
package group.assignment.booking_hotel_backend.services;

import group.assignment.booking_hotel_backend.models.Booking;

public interface FirebaseMessagingService {
    void sendBookingStatusUpdateNotification(Booking booking);
    void sendNotificationToUser(Integer userId, String title, String body);
    void sendNotificationToTokens(java.util.List<String> tokens, String title, String body);
}