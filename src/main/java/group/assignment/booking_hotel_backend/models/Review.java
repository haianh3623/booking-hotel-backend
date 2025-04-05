package group.assignment.booking_hotel_backend.models;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends BaseEntity{
    @Id
    private Integer reviewId;
    private String content;
    private Integer rating;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;


}