package group.assignment.booking_hotel_backend.dto;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingStatsDto {
    private LocalDate date;
    private Long count;
}
