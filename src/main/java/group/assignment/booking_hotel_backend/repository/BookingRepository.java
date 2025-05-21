package group.assignment.booking_hotel_backend.repository;
import group.assignment.booking_hotel_backend.models.Booking;
import group.assignment.booking_hotel_backend.models.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

    @Query("SELECT b FROM Booking b WHERE b.room.hotel.hotelId = :hotelId AND LOWER(b.user.fullName) LIKE %:query%")
    Page<Booking> findByHotelIdAndQuery(@Param("hotelId") Integer hotelId, @Param("query") String query, Pageable pageable);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.room.hotel.hotelId = :hotelId")
    Long countByHotelId(@Param("hotelId") Integer hotelId);


    List<Booking> findByCheckInBetweenAndStatus(LocalDateTime start, LocalDateTime end, BookingStatus status);
    @Query("SELECT SUM(b.price) FROM Booking b " +
            "JOIN b.bill bi " +
            "WHERE b.checkIn BETWEEN :startDate AND :endDate " +
            "AND b.status = 'CONFIRMED' " +
            "AND bi.paidStatus = true")
    Double getTotalRevenueBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT b FROM Booking b WHERE b.checkIn >= :startDate AND b.checkOut <= :endDate " +
            "AND b.status = 'CONFIRMED' AND b.bill.paidStatus = true")
    List<Booking> findBookingsBetweenDates(@Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    @Query("SELECT b FROM Booking b WHERE b.room.hotel.user.userId = :userId")
    List<Booking> findAllBookingsByHotelOwner(@Param("userId") Integer userId);


    @Query("SELECT b FROM Booking b WHERE b.room.roomId = :roomId AND b.status IN :statuses")
    List<Booking> findByRoomIdAndStatusIn(
            @Param("roomId") Integer roomId, 
            @Param("statuses") List<BookingStatus> statuses);

        @Query("SELECT b FROM Booking b JOIN b.room r JOIN b.user u " +
                "WHERE r.hotel.hotelId = :hotelId AND b.status = :status " +
                "AND (LOWER(r.roomName) LIKE LOWER(CONCAT('%', :query, '%')) " +
                "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Booking> findByHotelIdAndStatusAndRoomNameContainingOrUserFullNameContaining(
            @Param("hotelId") Integer hotelId,
            @Param("status") BookingStatus status,
            @Param("query") String query,
            Pageable pageable);

        @Query("SELECT b FROM Booking b JOIN b.room r WHERE r.hotel.hotelId = :hotelId")
    Page<Booking> findByHotelId(@Param("hotelId") Integer hotelId, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN b.room r JOIN b.user u " +
           "WHERE r.hotel.hotelId = :hotelId " +
           "AND (LOWER(r.roomName) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Booking> findByHotelIdAndRoomNameContainingOrUserFullNameContaining(
            @Param("hotelId") Integer hotelId,
            @Param("query") String query,
            Pageable pageable);

 @Query("SELECT b FROM Booking b JOIN b.room r " +
           "WHERE r.hotel.hotelId = :hotelId AND b.status = :status")
    Page<Booking> findByHotelIdAndStatus(
            @Param("hotelId") Integer hotelId,
            @Param("status") BookingStatus status,
            Pageable pageable);
}
