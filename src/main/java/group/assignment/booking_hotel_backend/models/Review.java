package group.assignment.booking_hotel_backend.models;
import jakarta.persistence.*;
import lombok.*;


// Backend: Update Review.java model to include ownerReply field
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewId;
    private String content;
    private Integer rating;
    
    // Add this field for owner replies
    @Column(length = 1000)
    private String ownerReply;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
}