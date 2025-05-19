package group.assignment.booking_hotel_backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
public class ReviewRequestDto {
    private String content;
    private Integer rating;
    private Integer bookingId;
}
