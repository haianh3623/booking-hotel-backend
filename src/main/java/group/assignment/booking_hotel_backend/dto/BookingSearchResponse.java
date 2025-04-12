package group.assignment.booking_hotel_backend.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BookingSearchResponse {
    private Integer roomId;
    private String roomName;
    private Double price;
    private String hotelName;
    private String roomImg;
    private String address;
    private List<String> services;
    private String checkIn;
    private String checkOut;
    private int adults;
    private int children;
    private int bedNumber;
}
