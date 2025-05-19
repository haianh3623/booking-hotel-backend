package group.assignment.booking_hotel_backend.dto;

import group.assignment.booking_hotel_backend.models.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDetailsDto {
    private Integer roomId;
    private String roomName;
    private Double area;
    private Double comboPrice2h;
    private Double pricePerNight;
    private Double extraHourPrice;
    private Integer standardOccupancy;
    private Integer maxOccupancy;
    private Integer numChildrenFree;
    private List<String> roomImgs;
    private Integer bedNumber;
    private Double extraAdult;
    private String description;
    private String hotelName;
    private String address;
    private List<String> services;
    private List<ReviewCardDto> reviews;

}
