package group.assignment.booking_hotel_backend.services.impl;

import group.assignment.booking_hotel_backend.models.RoomImage;
import group.assignment.booking_hotel_backend.repository.RoomImageRepository;
import group.assignment.booking_hotel_backend.services.RoomImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoomImageServiceImpl implements RoomImageService {
    private final RoomImageRepository roomImageRepository;
    @Override
    public void save(RoomImage roomImage) {
        roomImageRepository.save(roomImage);
    }

    @Override
    public void findByRoomRoomId(Integer roomId) {
        roomImageRepository.findByRoomRoomId(roomId);
    }
}
