package group.assignment.booking_hotel_backend.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BookingSearchRequest {
    private String infoSearch;
    private String city;
    private String district;
    private String checkInDate;
    private String checkOutDate;
    private String checkInTime;
    private String checkOutTime;
    private int adults;
    private int children;
    private int bedNumber;
    private Double priceFrom;
    private Double priceTo;
    private String sortBy; // "price_asc", "price_desc", "rating_desc", "rating_asc"
    private List<String> services;
}
