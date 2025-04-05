package group.assignment.booking_hotel_backend.repository;
import group.assignment.booking_hotel_backend.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Integer> {
}
