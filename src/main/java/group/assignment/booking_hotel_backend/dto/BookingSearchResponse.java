package group.assignment.booking_hotel_backend.dto;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingSearchResponse {
    private Integer roomId;
    private String roomName;
    private Double price;
    private String hotelName;
    private String address;
    private List<String> services;
}
