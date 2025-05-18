package group.assignment.booking_hotel_backend.services.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import group.assignment.booking_hotel_backend.models.Booking;
import group.assignment.booking_hotel_backend.models.DeviceToken;
import group.assignment.booking_hotel_backend.services.DeviceTokenService;
import group.assignment.booking_hotel_backend.services.FirebaseMessagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseMessagingServiceImpl implements FirebaseMessagingService {
    
    private final DeviceTokenService deviceTokenService;
    private final FirebaseMessaging firebaseMessaging;
    
    @Override
    @Async
    public void sendBookingStatusUpdateNotification(Booking booking) {
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
            
            // Send notification
            sendNotificationToTokens(tokens, title, body, buildNotificationData(booking));
            
            log.info("Booking status notification sent to user {} for booking {}", 
                    userId, booking.getBookingId());
            
        } catch (Exception e) {
            log.error("Error sending booking status notification: {}", e.getMessage(), e);
        }
    }
    
    @Override
    @Async
    public void sendNotificationToUser(Integer userId, String title, String body) {
        try {
            List<DeviceToken> deviceTokens = deviceTokenService.getActiveTokensByUserId(userId);
            
            if (deviceTokens.isEmpty()) {
                log.warn("No active device tokens found for user: {}", userId);
                return;
            }
            
            List<String> tokens = deviceTokens.stream()
                    .map(DeviceToken::getDeviceToken)
                    .collect(Collectors.toList());
            
            sendNotificationToTokens(tokens, title, body);
            
        } catch (Exception e) {
            log.error("Error sending notification to user {}: {}", userId, e.getMessage(), e);
        }
    }
    
    @Override
    @Async
    public void sendNotificationToTokens(List<String> tokens, String title, String body) {
        sendNotificationToTokens(tokens, title, body, null);
    }
    
    private void sendNotificationToTokens(List<String> tokens, String title, String body, Map<String, String> data) {
        try {
            if (tokens.isEmpty()) {
                log.warn("No tokens provided for notification");
                return;
            }
            
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
            
            firebaseMessaging.sendMulticastAsync(message)
                    .addCallback(
                            result -> {
                                log.info("Successfully sent notification. Success count: {}, Failure count: {}", 
                                        result.getSuccessCount(), result.getFailureCount());
                                
                                // Handle failed tokens (optional: remove invalid tokens)
                                if (result.getFailureCount() > 0) {
                                    log.warn("Some notifications failed to send: {}", result.getResponses());
                                }
                            },
                            failure -> log.error("Failed to send notification: {}", failure.getMessage(), failure)
                    );
                    
        } catch (Exception e) {
            log.error("Error sending multicast notification: {}", e.getMessage(), e);
        }
    }
    
    private String getNotificationTitle(Booking booking) {
        switch (booking.getStatus()) {
            case CONFIRMED:
                return "Đặt phòng được xác nhận";
            case CANCELLED:
                return "Đặt phòng bị hủy";
            default:
                return "Cập nhật đặt phòng";
        }
    }
    
    private String getNotificationBody(Booking booking) {
        switch (booking.getStatus()) {
            case CONFIRMED:
                return String.format("Đặt phòng #%d tại %s đã được xác nhận. Vui lòng chuẩn bị cho chuyến đi của bạn!",
                        booking.getBookingId(), booking.getRoom().getHotel().getHotelName());
            case CANCELLED:
                return String.format("Đặt phòng #%d tại %s đã bị hủy. Vui lòng liên hệ khách sạn nếu có thắc mắc.",
                        booking.getBookingId(), booking.getRoom().getHotel().getHotelName());
            default:
                return String.format("Đặt phòng #%d đã được cập nhật trạng thái.", booking.getBookingId());
        }
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