//package group.assignment.booking_hotel_backend.controller;
//import group.assignment.booking_hotel_backend.models.Review;
//import group.assignment.booking_hotel_backend.services.ReviewService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/reviews")
//public class ReviewController {
//
//    private final ReviewService reviewService;
//
//    @Autowired
//    public ReviewController(ReviewService reviewService) {
//        this.reviewService = reviewService;
//    }
//
//    @PostMapping
//    public ResponseEntity<Review> createReview(@RequestBody Review review) {
//        Review savedReview = reviewService.save(review);
//        return ResponseEntity.ok(savedReview);
//    }
//
//    @GetMapping("/{reviewId}")
//    public ResponseEntity<Review> getReviewById(@PathVariable Integer reviewId) {
//        Review review = reviewService.findById(reviewId);
//        return review.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @GetMapping("/booking/{bookingId}")
//    public ResponseEntity<List<Review>> getReviewsByBookingId(@PathVariable Integer bookingId) {
//        List<Review> reviews = reviewService.getReviewsByBookingId(bookingId);
//        return ResponseEntity.ok(reviews);
//    }
//
//    @PutMapping("/{reviewId}")
//    public ResponseEntity<Review> updateReview(@PathVariable Integer reviewId, @RequestBody Review review) {
//        Review updatedReview = reviewService.updateReview(reviewId, review);
//        return updatedReview != null ? ResponseEntity.ok(updatedReview) : ResponseEntity.notFound().build();
//    }
//
//    @DeleteMapping("/{reviewId}")
//    public ResponseEntity<Void> deleteReview(@PathVariable Integer reviewId) {
//        return reviewService.deleteReview(reviewId) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
//    }
//}
