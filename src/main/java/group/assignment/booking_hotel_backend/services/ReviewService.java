package group.assignment.booking_hotel_backend.services;
import group.assignment.booking_hotel_backend.dto.ReviewDto;
import group.assignment.booking_hotel_backend.dto.ReviewRequestDto;
import group.assignment.booking_hotel_backend.models.Review;
import java.util.List;
public interface ReviewService {
    Review save(Review review);

    void save(ReviewRequestDto request);
    List<Review> findAll();
    Review findById(Integer id);
    void deleteById(Integer id);
    List<Review> getReviewsByBookingId(Integer bookingId);
    Review update(Integer reviewId, ReviewRequestDto review);

    List<ReviewDto> findByRoomId(Integer roomId);
}
