package group.assignment.booking_hotel_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomSearchListDto {
    private Integer roomId;
    private String roomName;
    private String hotelName;
    private Double area;
    private Integer standardOccupancy;
    private String roomImg;
    private Integer bedNumber;
    private Double rating;
    private Integer reviewCount;
    private String address;

    RoomSearchListDto(Integer roomId, String roomName, String hotelName, Double area, Integer standardOccupancy,
                     String roomImg, Integer bedNumber, Double rating, Integer reviewCount) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.hotelName = hotelName;
        this.area = area;
        this.standardOccupancy = standardOccupancy;
        this.roomImg = roomImg;
        this.bedNumber = bedNumber;
        this.rating = rating;
        this.reviewCount = reviewCount;
    }
}
