package group.assignment.booking_hotel_backend.services.impl;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.firebase.messaging.*;
import group.assignment.booking_hotel_backend.models.Booking;
import group.assignment.booking_hotel_backend.models.DeviceToken;
import group.assignment.booking_hotel_backend.services.DeviceTokenService;
import group.assignment.booking_hotel_backend.services.FirebaseMessagingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@ConditionalOnBean(FirebaseMessaging.class)
@Slf4j
public class FirebaseMessagingServiceImpl implements FirebaseMessagingService {

    private final DeviceTokenService deviceTokenService;
    private final FirebaseMessaging firebaseMessaging;

    @Autowired
    public FirebaseMessagingServiceImpl(DeviceTokenService deviceTokenService, 
                                       @Autowired(required = false) FirebaseMessaging firebaseMessaging) {
        this.deviceTokenService = deviceTokenService;
        this.firebaseMessaging = firebaseMessaging;
        
        if (firebaseMessaging == null) {
            log.warn("FirebaseMessaging is not available. Push notifications will be disabled.");
        } else {
            log.info("FirebaseMessagingService initialized successfully");
        }
    }

    @Override
    public void sendBookingStatusUpdateNotification(Booking booking) {
        if (!isFirebaseEnabled()) {
            log.warn("Firebase messaging is not enabled, skipping notification");
            return;
        }

        try {
            Integer userId = booking.getUser().getUserId();
            String title = getNotificationTitle(booking);
            String body = getNotificationBody(booking);
            Map<String, String> data = buildNotificationData(booking);

            log.info("Sending booking status notification for booking ID: {}, user ID: {}", 
                    booking.getBookingId(), userId);
            
            sendToUser(userId, title, body, data);
        } catch (Exception e) {
            log.error("Error in sendBookingStatusUpdateNotification for booking {}: {}", 
                    booking.getBookingId(), e.getMessage(), e);
        }
    }

    @Override
    public void sendNotificationToUser(Integer userId, String title, String body) {
        if (!isFirebaseEnabled()) {
            log.warn("Firebase messaging is not enabled, skipping notification");
            return;
        }

        sendToUser(userId, title, body, null);
    }

    @Override
    public void sendNotificationToTokens(List<String> tokens, String title, String body) {
        if (tokens == null || tokens.isEmpty() || !isFirebaseEnabled()) {
            log.warn("No tokens provided or Firebase not enabled, skipping notification");
            return;
        }

        sendNotificationToTokensAsync(tokens, title, body, null)
                .thenAccept(this::handleBatchResult)
                .exceptionally(e -> { 
                    log.error("Send notification error: ", e); 
                    return null; 
                });
    }

    private void sendToUser(Integer userId, String title, String body, Map<String, String> data) {
        if (!isFirebaseEnabled()) {
            log.warn("Firebase messaging is not enabled");
            return;
        }

        try {
            List<String> tokens = deviceTokenService.getActiveTokensByUserId(userId).stream()
                    .map(DeviceToken::getDeviceToken)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (tokens.isEmpty()) {
                log.warn("No active device tokens found for user {}", userId);
                return;
            }

            log.info("Sending notification to user {} with {} tokens", userId, tokens.size());
            
            sendNotificationToTokensAsync(tokens, title, body, data)
                    .thenAccept(response -> {
                        handleBatchResult(response);
                        log.info("Notification sent to user {} successfully", userId);
                    })
                    .exceptionally(e -> { 
                        log.error("Failed to send notification to user {}: {}", userId, e.getMessage(), e); 
                        return null; 
                    });
                    
        } catch (Exception e) {
            log.error("Error sending notification to user {}: {}", userId, e.getMessage(), e);
        }
    }

    private CompletableFuture<BatchResponse> sendNotificationToTokensAsync(
            List<String> tokens, String title, String body, Map<String, String> data) {

        if (!isFirebaseEnabled()) {
            return CompletableFuture.completedFuture(null);
        }

        List<Message> messages = tokens.stream()
                .map(token -> {
                    Message.Builder builder = Message.builder()
                            .setToken(token)
                            .setNotification(Notification.builder()
                                    .setTitle(title)
                                    .setBody(body)
                                    .build());
                    
                    if (data != null && !data.isEmpty()) {
                        builder.putAllData(data);
                    }
                    
                    return builder.build();
                })
                .collect(Collectors.toList());

        try {
            log.debug("Attempting to send {} messages via Firebase", messages.size());
            ApiFuture<BatchResponse> future = firebaseMessaging.sendEachAsync(messages);
            
            CompletableFuture<BatchResponse> completableFuture = new CompletableFuture<>();
            ApiFutures.addCallback(future, new ApiFutureCallback<BatchResponse>() {
                @Override
                public void onSuccess(BatchResponse result) { 
                    log.debug("Firebase batch send completed successfully");
                    completableFuture.complete(result); 
                }
                
                @Override
                public void onFailure(Throwable t) {
                    log.error("Firebase sendEachAsync failed", t);
                    completableFuture.completeExceptionally(t);
                }
            }, Runnable::run);
            
            return completableFuture;
        } catch (Exception e) {
            log.error("Error sending batch notification", e);
            return CompletableFuture.completedFuture(null);
        }
    }

    private void handleBatchResult(BatchResponse response) {
        if (response == null) {
            log.warn("Firebase batch response is null");
            return;
        }

        int successCount = response.getSuccessCount();
        int failureCount = response.getFailureCount();
        
        log.info("Firebase batch result: {} successful, {} failed", successCount, failureCount);

        if (failureCount > 0) {
            List<String> invalidTokens = new ArrayList<>();
            
            for (int i = 0; i < response.getResponses().size(); i++) {
                SendResponse res = response.getResponses().get(i);
                if (!res.isSuccessful()) {
                    FirebaseMessagingException exception = res.getException();
                    String errorCode = exception.getMessagingErrorCode() != null 
                            ? exception.getMessagingErrorCode().name() 
                            : "UNKNOWN";
                            
                    log.warn("Message failed with error code {}: {}", errorCode, exception.getMessage());
                    
                    // Collect invalid tokens for cleanup
                    if ("UNREGISTERED".equals(errorCode) || "INVALID_TOKEN".equals(errorCode)) {
                        // You'd need to track which token corresponds to which response
                        // This is a limitation of the current implementation
                        log.warn("Invalid token detected at index {}", i);
                    }
                }
            }
            
            // Clean up invalid tokens if any were found
            if (!invalidTokens.isEmpty()) {
                try {
                    deviceTokenService.cleanupInvalidTokens(invalidTokens);
                } catch (Exception e) {
                    log.error("Error cleaning up invalid tokens: {}", e.getMessage());
                }
            }
        }
    }

    private boolean isFirebaseEnabled() {
        boolean enabled = firebaseMessaging != null && deviceTokenService != null;
        if (!enabled) {
            log.debug("Firebase messaging not enabled: messaging={}, deviceTokenService={}", 
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
                    booking.getBookingId(), 
                    booking.getRoom().getHotel().getHotelName());
            case CANCELLED -> String.format(
                    "Đặt phòng #%d tại %s đã bị hủy. Vui lòng liên hệ khách sạn nếu có thắc mắc.",
                    booking.getBookingId(), 
                    booking.getRoom().getHotel().getHotelName());
            default -> String.format("Đặt phòng #%d đã được cập nhật trạng thái.", booking.getBookingId());
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