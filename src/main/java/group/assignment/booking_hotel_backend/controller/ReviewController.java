package group.assignment.booking_hotel_backend.controller;
import group.assignment.booking_hotel_backend.dto.ReviewRequestDto;
import group.assignment.booking_hotel_backend.dto.ReviewResponseDto;
import group.assignment.booking_hotel_backend.mapper.BookingMapper;
import group.assignment.booking_hotel_backend.mapper.ReviewMapper;
import group.assignment.booking_hotel_backend.models.Booking;
import group.assignment.booking_hotel_backend.models.Review;
import group.assignment.booking_hotel_backend.services.BookingService;
import group.assignment.booking_hotel_backend.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final BookingService bookingService;


    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(@RequestBody ReviewRequestDto reviewRequestDto) {
        Booking booking = bookingService.findById(reviewRequestDto.getBookingId());
        Review savedReview = reviewService.save(ReviewMapper.mapToReview(reviewRequestDto, new Review(), booking));
        return ResponseEntity.ok(ReviewMapper.mapToReviewResponseDto(savedReview, new ReviewResponseDto()));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> getReviewById(@PathVariable Integer reviewId) {
        Review review = reviewService.findById(reviewId);
        return ResponseEntity.ok(ReviewMapper.mapToReviewResponseDto(review, new ReviewResponseDto()));
    }

    @PostMapping("/listId")
    public ResponseEntity<List<ReviewResponseDto>> getReviewByListId(@RequestBody List<Integer> reviewId) {
        List<ReviewResponseDto> reviewList = new ArrayList<>();
        for (Integer id : reviewId) {
            Review review = reviewService.findById(id);
            reviewList.add(ReviewMapper.mapToReviewResponseDto(review, new ReviewResponseDto()));
        }
        return ResponseEntity.ok(reviewList);
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByBookingId(@PathVariable Integer bookingId) {
        List<Review> reviews = reviewService.getReviewsByBookingId(bookingId);
        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();
        for (Review review : reviews) {
            reviewResponseDtoList.add(ReviewMapper.mapToReviewResponseDto(review, new ReviewResponseDto()));
        }
        return ResponseEntity.ok(reviewResponseDtoList);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(@PathVariable Integer reviewId, @RequestBody ReviewRequestDto review) {
        Review updatedReview = reviewService.update(reviewId, review);
        return updatedReview != null ? ResponseEntity.ok(ReviewMapper.mapToReviewResponseDto(updatedReview, new ReviewResponseDto())) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer reviewId) {
        reviewService.deleteById(reviewId);
        return ResponseEntity.noContent().build();
    }
}


