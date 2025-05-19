package group.assignment.booking_hotel_backend.dto;

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
public class AdminRevenueResponse {
    private List<AdminBookingDetailResponse> bookingDetailList;
    private Double totalRevenue;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
