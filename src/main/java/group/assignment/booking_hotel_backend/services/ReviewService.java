package group.assignment.booking_hotel_backend.services;
import group.assignment.booking_hotel_backend.dto.ReviewRequestDto;
import group.assignment.booking_hotel_backend.dto.ReviewStatsDto;
import group.assignment.booking_hotel_backend.models.Review;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    Review save(Review review);

    void save(ReviewRequestDto request);
    List<Review> findAll();
    Review findById(Integer id);
    void deleteById(Integer id);
    List<Review> getReviewsByBookingId(Integer bookingId);
    Review update(Integer reviewId, ReviewRequestDto review);
    ReviewStatsDto getMonthlyReviewStats(Integer hotel_id);

    Page<Review> getReviewsByHotelId(Integer hotelId, String query, Integer rating, Pageable pageable);
}
