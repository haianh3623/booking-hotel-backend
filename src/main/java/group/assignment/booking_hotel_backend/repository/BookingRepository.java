package group.assignment.booking_hotel_backend.repository;
import group.assignment.booking_hotel_backend.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByRoomRoomIdAndCheckOutAfterAndCheckInBefore(
            Integer roomId, LocalDateTime checkIn, LocalDateTime checkOut
    );

    List<Booking> findByUserUserId(Integer userId);

    @Query("""
        SELECT DATE(b.createdAt) AS date, COUNT(b) AS count
        FROM Booking b
        JOIN b.room r
        WHERE b.createdAt >= :startDate AND r.hotel.hotelId = :hotelId
        GROUP BY DATE(b.createdAt)
        ORDER BY date DESC
    """)
    List<Object[]> countBookingsPerDayForHotel(
        @Param("hotelId") Integer hotelId,
        @Param("startDate") LocalDateTime startDate
    );

    @Query("""
        SELECT b FROM Booking b 
        JOIN b.room r 
        WHERE r.hotel.hotelId = :hotelId AND b.status = 'UNCONFIRMED'
    """)
    List<Booking> findConfirmedBookingsByHotelId(@Param("hotelId") Integer hotelId);


}
