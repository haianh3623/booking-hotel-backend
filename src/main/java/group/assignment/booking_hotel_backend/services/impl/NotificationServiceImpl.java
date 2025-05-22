package group.assignment.booking_hotel_backend.services.impl;

import group.assignment.booking_hotel_backend.dto.NotificationDto;
import group.assignment.booking_hotel_backend.models.Booking;
import group.assignment.booking_hotel_backend.models.Notification;
import group.assignment.booking_hotel_backend.repository.NotificationRepository;
import group.assignment.booking_hotel_backend.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Override
    public void handleBookingEvent(Booking booking, String eventType) {
        switch (eventType) {
            case "BOOKING_CONFIRM":
                createBookingSuccessNotification(booking);
                createCheckInOutNotifications(booking);
                break;
            case "BOOKING_CANCEL":
                createBookingCancelNotification(booking);
                break;
            default:
                break;
        }
    }

    private void createBookingSuccessNotification(Booking booking) {
        Notification notification = Notification.builder()
                .user(booking.getUser())
                .booking(booking)
                .content(String.format("Quý khách đã đặt phòng thành công tại %s từ %s đến %s.",
                        booking.getRoom().getRoomName(),
                        formatDateTime(booking.getCheckIn()),
                        formatDateTime(booking.getCheckOut())))
                .notificationTime(LocalDateTime.now()) // Thông báo ngay lập tức
                .build();
        notificationRepository.save(notification);
    }

    private void createCheckInOutNotifications(Booking booking) {
        // Thông báo check-in (trước 24h)
        LocalDateTime checkInTime = booking.getCheckIn();
        Notification checkInNotification = Notification.builder()
                .user(booking.getUser())
                .booking(booking)
                .content(String.format("Xin chào %s, quý khách có lịch check-in vào lúc %s tại phòng %s, vui lòng mang theo giấy tờ tùy thân.",
                        booking.getUser().getFullName(),
                        formatDateTime(checkInTime),
                        booking.getRoom().getRoomName()))
                .notificationTime(checkInTime.minusHours(24))
                .build();
        checkInNotification.setCreatedAt(checkInTime.minusHours(24));
        notificationRepository.save(checkInNotification);

        // Thông báo check-out (trước 30p)
        LocalDateTime checkOutTime = booking.getCheckOut();
        Notification checkOutNotification = Notification.builder()
                .user(booking.getUser())
                .booking(booking)
                .content(String.format("Xin chào %s, quý khách có lịch check-out vào lúc %s tại phòng %s, vui lòng kiểm tra lại phòng trước khi rời đi.",
                        booking.getUser().getFullName(),
                        formatDateTime(checkOutTime),
                        booking.getRoom().getRoomName()))
                .notificationTime(checkOutTime.minusMinutes(30))
                .build();
        checkOutNotification.setCreatedAt(checkOutTime.minusMinutes(30));
        notificationRepository.save(checkOutNotification);
    }

    private void createBookingCancelNotification(Booking booking) {
        Notification notification = Notification.builder()
                .user(booking.getUser())
                .booking(booking)
                .content(String.format("Quý khách đã hủy đặt phòng tại %s từ %s đến %s.",
                        booking.getRoom().getRoomName(),
                        formatDateTime(booking.getCheckIn()),
                        formatDateTime(booking.getCheckOut())))
                .notificationTime(LocalDateTime.now()) // Thông báo ngay lập tức
                .build();
        notificationRepository.save(notification);
    }

    @Override
    public void deleteNotificationsByBookingId(Integer bookingId) {
        notificationRepository.deleteByBooking_BookingId(bookingId);
    }

    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        return dateTime.format(formatter);
    }

    @Override
    public List<NotificationDto> getUserNotifications(Integer userId) {
        LocalDateTime now = LocalDateTime.now();
        List<Notification> notifications = notificationRepository.findByUser_UserIdAndNotificationTimeLessThanEqualOrNotificationTimeIsNull(userId, now);
        System.out.println("User ID: " + userId);
        System.out.println("Notifications: " + notifications);
        return notifications.stream()
                .map(n -> new NotificationDto(
                        n.getNotiId(),
                        n.getContent(),
                        n.getNotificationTime(),
                        n.getUser().getUserId(),
                        n.getCreatedAt() != null ? n.getCreatedAt() : LocalDateTime.now() // Set createdAt to now if null
                ))
                .collect(Collectors.toList());
//        return notifications.stream()
//                .filter(n -> n.getNotificationTime() == null || n.getNotificationTime().isBefore(now))
//                .collect(Collectors.toList());
    }
}