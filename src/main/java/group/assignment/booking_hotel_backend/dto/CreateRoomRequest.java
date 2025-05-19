package group.assignment.booking_hotel_backend.dto;

import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
public class CreateRoomRequest {
    private String roomName;
    private Double area;
    private Double comboPrice2h;
    private Double pricePerNight;
    private Double extraHourPrice;
    private Integer standardOccupancy;
    private Integer maxOccupancy;
    private Integer numChildrenFree;
    private Integer bedNumber;
    private Double extraAdult;
    private String description;
    private Integer hotelId;
    private List<Integer> serviceIds;

    // Mapping to JSON for handling in the controller
    public Map<String, Object> toJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("roomName", roomName);
        map.put("area", area);
        map.put("comboPrice2h", comboPrice2h);
        map.put("pricePerNight", pricePerNight);
        map.put("extraHourPrice", extraHourPrice);
        map.put("standardOccupancy", standardOccupancy);
        map.put("maxOccupancy", maxOccupancy);
        map.put("numChildrenFree", numChildrenFree);
        map.put("bedNumber", bedNumber);
        map.put("extraAdult", extraAdult);
        map.put("description", description);
        map.put("hotelId", hotelId);
        map.put("serviceIds", serviceIds);
        return map;
    }
}