package group.assignment.booking_hotel_backend.general.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Integer reviewId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "time_created")
    private LocalDateTime timeCreated;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    // Default constructor
    public Review() {
    }

    // Constructor with required fields
    public Review(String content, Integer rating, Booking booking) {
        this.content = content;
        this.rating = rating;
        this.booking = booking;
        this.timeCreated = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.rating = rating;
    }

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(LocalDateTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    @PrePersist
    protected void onCreate() {
        if (timeCreated == null) {
            timeCreated = LocalDateTime.now();
        }
    }
}