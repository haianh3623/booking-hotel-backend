package group.assignment.booking_hotel_backend.repository;
import group.assignment.booking_hotel_backend.models.Room;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    @Query("SELECT r FROM Room r WHERE r.hotel.hotelId = :hotelId AND LOWER(r.roomName) LIKE %:query%")
    Page<Room> findByHotelIdAndQuery(@Param("hotelId") Integer hotelId, @Param("query") String query, Pageable pageable);

    @Query("SELECT COUNT(r) FROM Room r WHERE r.hotel.hotelId = :hotelId")
    Long countByHotelId(@Param("hotelId") Integer hotelId);

}
