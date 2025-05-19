package group.assignment.booking_hotel_backend.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingScheduleResponse {
    private Integer bookingId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Double price;
    private String status;
    private UserDto userDto;
    private RoomResponseDto roomDto;
    private LocalDateTime createdAt;
}
