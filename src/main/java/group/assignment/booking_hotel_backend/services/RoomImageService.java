package group.assignment.booking_hotel_backend.services;

import group.assignment.booking_hotel_backend.models.RoomImage;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface RoomImageService {
    void save(RoomImage roomImage);
    List<RoomImage> findByRoomRoomId(Integer roomId);
    void deleteById(Integer id);
}
