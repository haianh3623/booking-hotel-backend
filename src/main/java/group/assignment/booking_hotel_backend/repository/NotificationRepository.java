package group.assignment.booking_hotel_backend.repository;
import group.assignment.booking_hotel_backend.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
