package group.assignment.booking_hotel_backend.repository;
import group.assignment.booking_hotel_backend.models.RoomImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomImageRepository extends JpaRepository<RoomImage, Integer> {
    List<RoomImage> findByRoomRoomId(Integer roomId);
}
