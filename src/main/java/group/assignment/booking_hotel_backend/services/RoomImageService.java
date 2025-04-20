package group.assignment.booking_hotel_backend.services;

import group.assignment.booking_hotel_backend.models.RoomImage;
import org.springframework.web.multipart.MultipartFile;

public interface RoomImageService {
    void save(RoomImage roomImage);
    void findByRoomRoomId(Integer roomId);
}
