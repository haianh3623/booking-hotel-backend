//package group.assignment.booking_hotel_backend.services.impl;
//
//import group.assignment.booking_hotel_backend.models.Booking;
//import group.assignment.booking_hotel_backend.models.Notification;
//import group.assignment.booking_hotel_backend.repository.NotificationRepository;
//import group.assignment.booking_hotel_backend.services.NotificationService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.TaskScheduler;
//import org.springframework.stereotype.Service;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.Date;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class NotificationServiceImpl implements NotificationService {
//    private final NotificationRepository notificationRepository;
//    private final TaskScheduler taskScheduler;
//
//    @Override
//    public void handleBookingEvent(Booking booking, String eventType) {
//        switch (eventType) {
//            case "BOOKING_SUCCESS":
//                createBookingSuccessNotification(booking);
//                scheduleCheckInOutNotifications(booking);
//                break;
//            case "BOOKING_CANCEL":
//                createBookingCancelNotification(booking);
//                break;
//        }
//    }
//
//    @Override
//    public List<Notification> getUserNotifications(Integer userId) {
//        return notificationRepository.findByUser_UserIdOrderByCreatedAtDesc(userId);
//    }
//
//    @Override
//    public Integer getNotificationCount(Integer userId) {
//        return notificationRepository.countByUser_UserId(userId);
//    }
//
//    @Override
//    public void scheduleCheckInOutNotifications(Booking booking) {
//        Date checkInReminder = convertToDateAndAddHours(booking.getCheckIn(), -24);
//        taskScheduler.schedule(() -> createCheckInReminder(booking), checkInReminder);
//
//        Date checkOutReminder = convertToDateAndAddHours(booking.getCheckOut(), -2);
//        taskScheduler.schedule(() -> createCheckOutReminder(booking), checkOutReminder);
//    }
//
//    private void createBookingSuccessNotification(Booking booking) {
//        Notification notification = Notification.builder()
//                .user(booking.getUser())
//                .content("Booking successful for room: " + booking.getRoom().getRoomName())
//                .build();
//        notificationRepository.save(notification);
//    }
//
//    private void createBookingCancelNotification(Booking booking) {
//        Notification notification = Notification.builder()
//                .user(booking.getUser())
//                .content("Booking cancelled for room: " + booking.getRoom().getRoomName())
//                .build();
//        notificationRepository.save(notification);
//    }
//
//    private void createCheckInReminder(Booking booking) {
//        Notification notification = Notification.builder()
//                .user(booking.getUser())
//                .content("Check-in reminder for room: " + booking.getRoom().getRoomName())
//                .build();
//        notificationRepository.save(notification);
//    }
//
//    private void createCheckOutReminder(Booking booking) {
//        Notification notification = Notification.builder()
//                .user(booking.getUser())
//                .content("Check-out reminder for room: " + booking.getRoom().getRoomName())
//                .build();
//        notificationRepository.save(notification);
//    }
//
//    private Date convertToDateAndAddHours(LocalDateTime localDateTime, int hours) {
//        LocalDateTime adjustedDateTime = localDateTime.plusHours(hours);
//        return Date.from(adjustedDateTime.atZone(ZoneId.systemDefault()).toInstant());
//    }
//}