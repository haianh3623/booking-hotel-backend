package group.assignment.booking_hotel_backend.services;

import group.assignment.booking_hotel_backend.dto.NotificationDto;
import group.assignment.booking_hotel_backend.models.Booking;
import group.assignment.booking_hotel_backend.models.Notification;
import java.util.List;

public interface NotificationService {
    void handleBookingEvent(Booking booking, String eventType);
    List<NotificationDto> getUserNotifications(Integer userId);
}