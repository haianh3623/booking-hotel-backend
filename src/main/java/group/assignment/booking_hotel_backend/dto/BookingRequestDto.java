package group.assignment.booking_hotel_backend.dto;

import java.time.LocalDateTime;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequestDto {
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Double price;
    private Integer userId;
    private Integer roomId;
    private Integer billId;
}