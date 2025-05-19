package group.assignment.booking_hotel_backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
public class AdminBookingDetailResponse {
    private Integer bookingId;
    private String roomName;
    private String fullName;
    private String phone;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Double price;
    private LocalDateTime createAt;
}
