package group.assignment.booking_hotel_backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {
    private String keyword;
    private String city;
    private String district;
    private Integer numOfAdult;
    private Integer numOfChild;
    private Integer numOfBed;
    private List<String> services;
}
