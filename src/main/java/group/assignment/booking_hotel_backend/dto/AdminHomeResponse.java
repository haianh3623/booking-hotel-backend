package group.assignment.booking_hotel_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminHomeResponse {
    private Long totalHotels;
    private Long totalUsers;
    private Long totalBookings;
}
