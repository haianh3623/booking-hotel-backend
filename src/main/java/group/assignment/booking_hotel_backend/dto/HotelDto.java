package group.assignment.booking_hotel_backend.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelDto {
    private Integer hotelId;
    private String hotelName;
    private AddressDto address;
    private Integer userId;
}
