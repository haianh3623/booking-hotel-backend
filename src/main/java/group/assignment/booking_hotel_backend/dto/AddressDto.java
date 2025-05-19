package group.assignment.booking_hotel_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {
    private String city;
    private String district;
    private String ward;
    private String specificAddress;

    @Override
    public String toString() {
        return specificAddress + ", " + ward + ", " + district + ", " + city;
    }
}
