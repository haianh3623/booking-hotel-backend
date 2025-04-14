package group.assignment.booking_hotel_backend.repository;
import group.assignment.booking_hotel_backend.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByBookingBookingId(Integer bookingId);
}
