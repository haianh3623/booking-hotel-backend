package group.assignment.booking_hotel_backend.mapper;
import group.assignment.booking_hotel_backend.dto.BookingDto;
import group.assignment.booking_hotel_backend.dto.ReviewDto;
import group.assignment.booking_hotel_backend.dto.ReviewRequestDto;
import group.assignment.booking_hotel_backend.dto.ReviewResponseDto;
import group.assignment.booking_hotel_backend.models.Booking;
import group.assignment.booking_hotel_backend.models.Review;

public class ReviewMapper {

    public static Review mapToReview(ReviewRequestDto request, Review review, Booking booking) {
        if (request == null || booking == null) return null;
        review.setContent(request.getContent());
        review.setRating(request.getRating());
        review.setBooking(booking);
        return review;
    }

    public static Review mapToReviewDto(Review review, ReviewDto reviewDto) {
        reviewDto.setReviewId(reviewDto.getReviewId());
        reviewDto.setContent(review.getContent());
        reviewDto.setRating(review.getRating());
        reviewDto.setBookingId(review.getBooking().getBookingId());
        return review;
    }

    public static ReviewResponseDto mapToReviewResponseDto(Review review, ReviewResponseDto dto) {
        if (review == null) return null;
        dto.setReviewId(review.getReviewId());
        dto.setContent(review.getContent());
        dto.setRating(review.getRating());
        dto.setBookingDto(BookingMapper.mapToBookingDto(review.getBooking(), new BookingDto()));
        return dto;
    }
}
