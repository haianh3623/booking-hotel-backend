package group.assignment.booking_hotel_backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelRequestDto {
    private String hotelName;
    private AddressDto address;
    private Integer userId;
}
