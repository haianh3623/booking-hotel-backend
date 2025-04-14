package group.assignment.booking_hotel_backend.dto;
import group.assignment.booking_hotel_backend.models.BookingStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
public class BookingDto {
    private Integer bookingId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Double price;
    private BookingStatus status;
    private Integer userId;
    private Integer roomId;
    private Integer billId;
    private LocalDateTime createdAt;
    private List<Integer> reviewIds;
}
