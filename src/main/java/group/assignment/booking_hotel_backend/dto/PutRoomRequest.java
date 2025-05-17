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
public class PutRoomRequest {
    private int roomId;
    private String roomName;
    private double area;
    private double comboPrice2h;
    private double pricePerNight;
    private double extraHourPrice;
    private int standardOccupancy;
    private int maxOccupancy;
    private int numChildrenFree;
    private int bedNumber;
    private double extraAdult;
    private String description;

    // Updated to include service IDs directly
    private List<Integer> serviceIds;

    // Image IDs to keep
    private List<Integer> roomImageUrls;

    // Mapping to JSON for handling in the controller
    public Map<String, Object> toJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("roomId", roomId);
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
        map.put("serviceIds", serviceIds);
        map.put("roomImageUrls", roomImageUrls);
        return map;
    }
}