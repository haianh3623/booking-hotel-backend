package group.assignment.booking_hotel_backend.services.impl;

import group.assignment.booking_hotel_backend.dto.ReviewDto;
import group.assignment.booking_hotel_backend.dto.ReviewRequestDto;
import group.assignment.booking_hotel_backend.dto.ReviewStatsDto;
import group.assignment.booking_hotel_backend.models.Review;
import group.assignment.booking_hotel_backend.repository.ReviewRepository;
import group.assignment.booking_hotel_backend.services.ReviewService;
import group.assignment.booking_hotel_backend.utils.ReviewUtils;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public void save(ReviewRequestDto request) {

    }

    @Override
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Override
    public Review findById(Integer id) {
        Optional<Review> result = reviewRepository.findById(id);
        Review review  = null;
        if(result.isPresent()){
            review = result.get();
        }
        else{
            throw new RuntimeException("Không thấy review có id là: " + id);
        }
        return review;
    }

    @Override
    public void deleteById(Integer id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public List<Review> getReviewsByBookingId(Integer bookingId) {
        return reviewRepository.findByBookingBookingId(bookingId);
    }

    @Override
    public Review update(Integer reviewId, ReviewRequestDto review) {
        Optional<Review> existingReviewOpt = reviewRepository.findById(reviewId);
        if (existingReviewOpt.isPresent()) {
            Review existingReview = existingReviewOpt.get();
            existingReview.setContent(review.getContent());
            existingReview.setRating(review.getRating());
            return reviewRepository.save(existingReview);
        }
        return null;
    }

    @Override
    public ReviewStatsDto getMonthlyReviewStats(Integer hotelId) {
        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();

        int lastMonth = currentMonth == 1 ? 12 : currentMonth - 1;
        int lastYear = currentMonth == 1 ? currentYear - 1 : currentYear;

        // Số lượng đánh giá
        Long currentCount = reviewRepository.countReviewsByHotelIdAndMonth(hotelId, currentMonth, currentYear);
        Long lastCount = reviewRepository.countReviewsByHotelIdAndMonth(hotelId, lastMonth, lastYear);

        // Tính phần trăm thay đổi số lượng đánh giá
        double reviewCountChange = (lastCount == 0)
            ? (currentCount > 0 ? 100.0 : 0.0)
            : ((currentCount - lastCount) / (double) lastCount) * 100.0;

        // Trung bình rating
        Double currentAvg = reviewRepository.averageRatingByHotelAndMonth(hotelId, currentMonth, currentYear);
        Double lastAvg = reviewRepository.averageRatingByHotelAndMonth(hotelId, lastMonth, lastYear);

        if (currentAvg == null) currentAvg = 0.0;
        if (lastAvg == null) lastAvg = 0.0;

        // Tính phần trăm thay đổi rating trung bình
        double avgRatingChange = (lastAvg == 0)
            ? (currentAvg > 0 ? 100.0 : 0.0)
            : ((currentAvg - lastAvg) / lastAvg) * 100.0;

        return new ReviewStatsDto(currentCount, reviewCountChange, currentAvg, avgRatingChange);
    }

    @Override
    public Page<Review> getReviewsByHotelId(Integer hotelId, String query, Integer rating, Pageable pageable) {
        return reviewRepository.findByHotelIdAndQueryAndRating(hotelId, query.toLowerCase(), rating, pageable);
    }
}