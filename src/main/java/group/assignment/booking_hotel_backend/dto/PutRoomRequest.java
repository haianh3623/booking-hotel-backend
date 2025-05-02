package group.assignment.booking_hotel_backend.dto;

import lombok.Data;
import java.util.List;

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

    // Danh sách ID của các service đi kèm
    private List<Integer> serviceDtoList;

    // Danh sách ID của các RoomImage muốn giữ lại
    private List<Integer> roomImageUrls;
}

