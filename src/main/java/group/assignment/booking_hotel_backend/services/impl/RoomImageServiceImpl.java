package group.assignment.booking_hotel_backend.services.impl;

import group.assignment.booking_hotel_backend.models.RoomImage;
import group.assignment.booking_hotel_backend.repository.RoomImageRepository;
import group.assignment.booking_hotel_backend.services.RoomImageService;
import lombok.AllArgsConstructor;

import java.util.List;

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
    public List<RoomImage> findByRoomRoomId(Integer roomId) {
        return roomImageRepository.findByRoomRoomId(roomId);
    }

    @Override
    public void deleteById(Integer id) {
        roomImageRepository.deleteById(id);
    }

}
