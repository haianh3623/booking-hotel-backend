package group.assignment.booking_hotel_backend.dto;
import group.assignment.booking_hotel_backend.models.Booking;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
public class ReviewDto {
    private Integer reviewId;
    private String content;
    private Integer rating;
    private Integer bookingId;
}
