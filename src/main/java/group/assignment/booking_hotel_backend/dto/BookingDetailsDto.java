package group.assignment.booking_hotel_backend.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
public class BookingDetailsDto {
    private Integer bookingId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Double price;
    private String status;
    private UserDto user;
    private String roomName;
    private BillResponseDto bill;
    private LocalDateTime createdAt;
    private List<Integer> reviewIdList;
}

