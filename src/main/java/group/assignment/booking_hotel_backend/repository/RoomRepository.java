package group.assignment.booking_hotel_backend.repository;
import group.assignment.booking_hotel_backend.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findAllByHotel_HotelId(Integer hotelId);
}
