package group.assignment.booking_hotel_backend.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingResponseDto {
    private Integer bookingId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Double price;
    private String status;
    private Integer userId;
    private Integer roomId;
    private Integer billId;
    private LocalDateTime createdAt;
    private List<Integer> reviewIdList;
}
