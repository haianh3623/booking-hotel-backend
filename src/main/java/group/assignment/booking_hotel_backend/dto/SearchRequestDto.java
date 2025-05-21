package group.assignment.booking_hotel_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequestDto {
    private String keyword;
    private String city;
    private String district;
    private Integer numOfAdult;
    private Integer numOfChild;
    private Integer numOfBed;
    private List<String> serviers;
}
