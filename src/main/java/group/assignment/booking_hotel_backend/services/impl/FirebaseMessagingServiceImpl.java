package group.assignment.booking_hotel_backend.services.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.BatchResponse;
import group.assignment.booking_hotel_backend.models.Booking;
import group.assignment.booking_hotel_backend.models.DeviceToken;
import group.assignment.booking_hotel_backend.services.DeviceTokenService;
import group.assignment.booking_hotel_backend.services.FirebaseMessagingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FirebaseMessagingServiceImpl implements FirebaseMessagingService {
    
    private final DeviceTokenService deviceTokenService;
    private final FirebaseMessaging firebaseMessaging;
    
    // Constructor-based dependency injection with optional dependencies
    public FirebaseMessagingServiceImpl(
            @Autowired(required = false) DeviceTokenService deviceTokenService,
            @Autowired(required = false) FirebaseMessaging firebaseMessaging) {
        this.deviceTokenService = deviceTokenService;
        this.firebaseMessaging = firebaseMessaging;
    }
    
    @Override
    @Async("taskExecutor")
    public void sendBookingStatusUpdateNotification(Booking booking) {
        if (!isFirebaseEnabled()) {
            log.debug("Firebase not enabled, skipping notification for booking {}", booking.getBookingId());
            return;
        }
        
        try {
            Integer userId = booking.getUser().getUserId();
            String title = getNotificationTitle(booking);
            String body = getNotificationBody(booking);
            
            // Get user's device tokens
            List<DeviceToken> deviceTokens = deviceTokenService.getActiveTokensByUserId(userId);
            
            if (deviceTokens.isEmpty()) {
                log.warn("No active device tokens found for user: {}", userId);
                return;
            }
            
            List<String> tokens = deviceTokens.stream()
                    .map(DeviceToken::getDeviceToken)
                    .collect(Collectors.toList());
            
            // Send notification asynchronously
            sendNotificationToTokensAsync(tokens, title, body, buildNotificationData(booking))
                .thenAccept(result -> {
                    log.info("Booking status notification sent to user {} for booking {}. Success: {}, Failures: {}", 
                            userId, booking.getBookingId(), result.getSuccessCount(), result.getFailureCount());
                })
                .exceptionally(throwable -> {
                    log.error("Error sending booking status notification for booking {}: {}", 
                            booking.getBookingId(), throwable.getMessage());
                    return null;
                });
            
        } catch (Exception e) {
            log.error("Error preparing booking status notification: {}", e.getMessage(), e);
        }
    }
    
    @Override
    @Async("taskExecutor")
    public void sendNotificationToUser(Integer userId, String title, String body) {
        if (!isFirebaseEnabled()) {
            log.debug("Firebase not enabled, skipping notification for user {}", userId);
            return;
        }
        
        try {
            List<DeviceToken> deviceTokens = deviceTokenService.getActiveTokensByUserId(userId);
            
            if (deviceTokens.isEmpty()) {
                log.warn("No active device tokens found for user: {}", userId);
                return;
            }
            
            List<String> tokens = deviceTokens.stream()
                    .map(DeviceToken::getDeviceToken)
                    .collect(Collectors.toList());
            
            sendNotificationToTokensAsync(tokens, title, body, null)
                .thenAccept(result -> {
                    log.info("Notification sent to user {}. Success: {}, Failures: {}", 
                            userId, result.getSuccessCount(), result.getFailureCount());
                })
                .exceptionally(throwable -> {
                    log.error("Error sending notification to user {}: {}", userId, throwable.getMessage());
                    return null;
                });
            
        } catch (Exception e) {
            log.error("Error preparing notification for user {}: {}", userId, e.getMessage(), e);
        }
    }
    
    @Override
    @Async("taskExecutor")
    public void sendNotificationToTokens(List<String> tokens, String title, String body) {
        sendNotificationToTokensAsync(tokens, title, body, null)
            .thenAccept(result -> {
                log.info("Notification sent to {} tokens. Success: {}, Failures: {}", 
                        tokens.size(), result.getSuccessCount(), result.getFailureCount());
            })
            .exceptionally(throwable -> {
                log.error("Error sending notification to tokens: {}", throwable.getMessage());
                return null;
            });
    }
    
    /**
     * Internal method to send notifications asynchronously and return a CompletableFuture
     */
    private CompletableFuture<BatchResponse> sendNotificationToTokensAsync(
            List<String> tokens, String title, String body, Map<String, String> data) {
        
        if (!isFirebaseEnabled()) {
            log.debug("Firebase not enabled, skipping notification");
            return CompletableFuture.completedFuture(null);
        }
        
        if (tokens.isEmpty()) {
            log.warn("No tokens provided for notification");
            return CompletableFuture.completedFuture(null);
        }
        
        try {
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();
            
            MulticastMessage.Builder messageBuilder = MulticastMessage.builder()
                    .setNotification(notification)
                    .addAllTokens(tokens);
            
            // Add data payload if provided
            if (data != null && !data.isEmpty()) {
                messageBuilder.putAllData(data);
            }
            
            MulticastMessage message = messageBuilder.build();
            
            // Return the CompletableFuture directly
            return firebaseMessaging.sendMulticastAsync(message)
                .toCompletableFuture()
                .thenApply(batchResponse -> {
                    // Handle failed tokens (optional: you can implement token cleanup here)
                    if (batchResponse.getFailureCount() > 0) {
                        log.warn("Some notifications failed to send. Failures: {} out of {}", 
                                batchResponse.getFailureCount(), tokens.size());
                        
                        // You can iterate through responses to identify failed tokens
                        // and potentially remove them from the database
                        // handleFailedTokens(tokens, batchResponse);
                    }
                    return batchResponse;
                });
                
        } catch (Exception e) {
            log.error("Error creating multicast message: {}", e.getMessage(), e);
            CompletableFuture<BatchResponse> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }
    
    /**
     * Optional: Handle failed tokens by removing them from the database
     */
    @SuppressWarnings("unused")
    private void handleFailedTokens(List<String> tokens, BatchResponse batchResponse) {
        if (deviceTokenService == null) return;
        
        List<com.google.firebase.messaging.SendResponse> responses = batchResponse.getResponses();
        for (int i = 0; i < responses.size(); i++) {
            com.google.firebase.messaging.SendResponse response = responses.get(i);
            if (!response.isSuccessful()) {
                String failedToken = tokens.get(i);
                String errorCode = response.getException() != null ? 
                    response.getException().getMessagingErrorCode().name() : "UNKNOWN";
                
                // Remove invalid tokens
                if ("INVALID_REGISTRATION_TOKEN".equals(errorCode) || 
                    "REGISTRATION_TOKEN_NOT_REGISTERED".equals(errorCode)) {
                    log.info("Removing invalid token: {}", failedToken);
                    // You would need to implement this method in DeviceTokenService
                    // deviceTokenService.removeToken(failedToken);
                }
            }
        }
    }
    
    private boolean isFirebaseEnabled() {
        boolean enabled = firebaseMessaging != null && deviceTokenService != null;
        if (!enabled) {
            log.debug("Firebase disabled - FirebaseMessaging: {}, DeviceTokenService: {}", 
                     firebaseMessaging != null, deviceTokenService != null);
        }
        return enabled;
    }
    
    private String getNotificationTitle(Booking booking) {
        return switch (booking.getStatus()) {
            case CONFIRMED -> "Đặt phòng được xác nhận";
            case CANCELLED -> "Đặt phòng bị hủy";
            default -> "Cập nhật đặt phòng";
        };
    }
    
    private String getNotificationBody(Booking booking) {
        return switch (booking.getStatus()) {
            case CONFIRMED -> String.format(
                "Đặt phòng #%d tại %s đã được xác nhận. Vui lòng chuẩn bị cho chuyến đi của bạn!",
                booking.getBookingId(), booking.getRoom().getHotel().getHotelName());
            case CANCELLED -> String.format(
                "Đặt phòng #%d tại %s đã bị hủy. Vui lòng liên hệ khách sạn nếu có thắc mắc.",
                booking.getBookingId(), booking.getRoom().getHotel().getHotelName());
            default -> String.format(
                "Đặt phòng #%d đã được cập nhật trạng thái.", booking.getBookingId());
        };
    }
    
    private Map<String, String> buildNotificationData(Booking booking) {
        Map<String, String> data = new HashMap<>();
        data.put("type", "booking_status_update");
        data.put("bookingId", booking.getBookingId().toString());
        data.put("status", booking.getStatus().name());
        data.put("hotelName", booking.getRoom().getHotel().getHotelName());
        data.put("roomName", booking.getRoom().getRoomName());
        return data;
    }
}