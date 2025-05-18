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
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FirebaseMessagingServiceImpl implements FirebaseMessagingService {

    private final DeviceTokenService deviceTokenService;
    private final FirebaseMessaging firebaseMessaging;

    @Autowired
    public FirebaseMessagingServiceImpl(DeviceTokenService deviceTokenService, FirebaseMessaging firebaseMessaging) {
        this.deviceTokenService = deviceTokenService;
        this.firebaseMessaging = firebaseMessaging;
    }

    @Override
    public void sendBookingStatusUpdateNotification(Booking booking) {
        if (!isFirebaseEnabled()) return;

        Integer userId = booking.getUser().getUserId();
        String title = getNotificationTitle(booking);
        String body = getNotificationBody(booking);
        Map<String, String> data = buildNotificationData(booking);

        sendToUser(userId, title, body, data);
    }

    @Override
    public void sendNotificationToUser(Integer userId, String title, String body) {
        sendToUser(userId, title, body, null);
    }

    @Override
    public void sendNotificationToTokens(List<String> tokens, String title, String body) {
        if (tokens == null || tokens.isEmpty() || !isFirebaseEnabled()) return;

        sendNotificationToTokensAsync(tokens, title, body, null)
                .thenAccept(this::handleResult)
                .exceptionally(e -> { log.error("Send notification error: ", e); return null; });
    }

    private void sendToUser(Integer userId, String title, String body, Map<String, String> data) {
        if (!isFirebaseEnabled()) return;

        List<String> tokens = deviceTokenService.getActiveTokensByUserId(userId).stream()
                .map(DeviceToken::getDeviceToken)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (tokens.isEmpty()) return;

        sendNotificationToTokensAsync(tokens, title, body, data)
                .thenAccept(this::handleResult)
                .exceptionally(e -> { log.error("Send notification error: ", e); return null; });
    }

    private CompletableFuture<BatchResponse> sendNotificationToTokensAsync(
            List<String> tokens, String title, String body, Map<String, String> data) {

        List<Message> messages = tokens.stream()
                .map(token -> {
                    Message.Builder builder = Message.builder()
                            .setToken(token)
                            .setNotification(Notification.builder().setTitle(title).setBody(body).build());
                    if (data != null) builder.putAllData(data);
                    return builder.build();
                })
                .collect(Collectors.toList());

        try {
            ApiFuture<BatchResponse> future = firebaseMessaging.sendEachAsync(messages);
            CompletableFuture<BatchResponse> completableFuture = new CompletableFuture<>();
            ApiFutures.addCallback(future, new ApiFutureCallback<>() {
                public void onSuccess(BatchResponse result) { completableFuture.complete(result); }
                public void onFailure(Throwable t) {
                    log.error("Firebase sendAllAsync failed", t);
                    completableFuture.completeExceptionally(t);
                }
            }, Runnable::run);
            return completableFuture;
        } catch (Exception e) {
            log.error("Error sending batch notification", e);
            return CompletableFuture.completedFuture(null);
        }
    }

    private void handleResult(BatchResponse response) {
        if (response == null || response.getFailureCount() == 0) return;

        for (int i = 0; i < response.getResponses().size(); i++) {
            SendResponse res = response.getResponses().get(i);
            if (!res.isSuccessful()) {
                log.warn("Notification failed: {}", res.getException().getMessage());
            }
        }
    }

    private boolean isFirebaseEnabled() {
        return firebaseMessaging != null && deviceTokenService != null;
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
