package group.assignment.booking_hotel_backend.dto;

import java.time.LocalDateTime;
import java.util.List;

import group.assignment.booking_hotel_backend.models.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
public class BookingHotelOwnerDto {
    private Integer bookingId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Double price;
    private String status;
    private UserDto user;
    private String roomName;
    private Integer billId;
    private LocalDateTime createdAt;
    private List<Integer> reviewIdList;
}
