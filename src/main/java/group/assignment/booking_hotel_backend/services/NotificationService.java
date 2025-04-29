package group.assignment.booking_hotel_backend.services;

import group.assignment.booking_hotel_backend.models.Booking;
import group.assignment.booking_hotel_backend.models.Notification;
import java.util.List;

public interface NotificationService {
    void handleBookingEvent(Booking booking, String eventType);
    List<Notification> getUserNotifications(Integer userId);
    Integer getNotificationCount(Integer userId);
    void scheduleCheckInOutNotifications(Booking booking);
}