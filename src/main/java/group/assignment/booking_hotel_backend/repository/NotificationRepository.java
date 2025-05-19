package group.assignment.booking_hotel_backend.repository;

import group.assignment.booking_hotel_backend.dto.NotificationDto;
import group.assignment.booking_hotel_backend.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
//    @Query("SELECT n FROM Notification n WHERE n.user.userId = :userId AND (n.notificationTime <= CURRENT_TIMESTAMP OR n.notificationTime IS NULL)")
//    List<Notification> findByUserIdAndNotificationTimeBeforeOrNull(@Param("userId") Integer userId);
//    List<Notification> findByUser_UserId(Integer userId);
    List<Notification> findByUser_UserIdAndNotificationTimeLessThanEqualOrNotificationTimeIsNull(Integer userId, LocalDateTime notificationTime);
}