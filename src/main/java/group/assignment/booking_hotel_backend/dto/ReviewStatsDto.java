package group.assignment.booking_hotel_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewStatsDto {
    private Long totalReviews;
    private Double percentageChange;
}
