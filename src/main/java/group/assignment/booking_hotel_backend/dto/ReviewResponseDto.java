package group.assignment.booking_hotel_backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
public class ReviewResponseDto {
    private Integer reviewId;
    private String content;
    private Integer rating;
    private BookingDto bookingDto;
}
