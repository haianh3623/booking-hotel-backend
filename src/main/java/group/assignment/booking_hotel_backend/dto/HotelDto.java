package group.assignment.booking_hotel_backend.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
public class HotelDto {
    private Integer hotelId;
    private String hotelName;
    private AddressDto address;
    private Integer userId;
}
