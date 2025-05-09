package group.assignment.booking_hotel_backend.repository;
import group.assignment.booking_hotel_backend.dto.ReviewCardDto;
import group.assignment.booking_hotel_backend.dto.ReviewDto;
import group.assignment.booking_hotel_backend.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByBookingBookingId(Integer bookingId);

    @Query("SELECT new group.assignment.booking_hotel_backend.dto.ReviewDto" +
            "(r.reviewId, r.content, r.rating, b.bookingId) " +
            "FROM Review r " +
            "JOIN r.booking b " +
            "WHERE b.room.roomId = :roomId")
    List<ReviewDto> findReviewsByRoomId(@Param("roomId") Integer roomId);

    @Query("SELECT new group.assignment.booking_hotel_backend.dto.ReviewCardDto" +
            "(r.reviewId, r.content, r.rating, u.fullName) " +
            "FROM Review r " +
            "JOIN r.booking b " +
            "JOIN b.user u " +
            "WHERE b.room.roomId = :roomId")
    List<ReviewCardDto> findReviewsCardByRoomId(Integer roomId);
}
