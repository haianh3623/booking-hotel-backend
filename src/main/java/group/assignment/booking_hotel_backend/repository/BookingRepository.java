package group.assignment.booking_hotel_backend.repository;
import group.assignment.booking_hotel_backend.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
}
