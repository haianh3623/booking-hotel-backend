package group.assignment.booking_hotel_backend.dto;

import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
public class RoomResponseDto {
    private String roomName;
    private Double area;
    private Double comboPrice2h;
    private Double pricePerNight;
    private Double extraHourPrice;
    private Integer standardOccupancy;
    private Integer maxOccupancy;
    private Integer numChildrenFree;
    private String roomImg;
    private Integer bedNumber;
    private Double extraAdult;
    private String description;
    private HotelDto hotelDto;
    private List<ServiceDto> serviceDtoList;
    private List<RoomImageDto> roomImageUrls;
}
