package group.assignment.booking_hotel_backend.repository;
import group.assignment.booking_hotel_backend.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
}
