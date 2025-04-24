package group.assignment.booking_hotel_backend.repository;
import group.assignment.booking_hotel_backend.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByBookingBookingId(Integer bookingId);

    @Query("SELECT r FROM Review r " +
            "JOIN r.booking b " +
            "WHERE b.room.roomId = :roomId")
    List<Review> findReviewsByRoomId(@Param("roomId") Integer roomId);
}
