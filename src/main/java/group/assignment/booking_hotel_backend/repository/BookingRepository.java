package group.assignment.booking_hotel_backend.repository;
import group.assignment.booking_hotel_backend.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByRoomRoomIdAndCheckOutAfterAndCheckInBefore(
            Integer roomId, LocalDateTime checkIn, LocalDateTime checkOut
    );
}
