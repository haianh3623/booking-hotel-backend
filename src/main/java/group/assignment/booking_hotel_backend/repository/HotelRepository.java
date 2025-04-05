package group.assignment.booking_hotel_backend.repository;

import group.assignment.booking_hotel_backend.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {
}
