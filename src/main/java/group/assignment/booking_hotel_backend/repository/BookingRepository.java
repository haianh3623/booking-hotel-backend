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
    // Lấy booking theo roomId và status
    @Query("SELECT b FROM Booking b WHERE b.room.roomId = :roomId AND b.status IN :statuses")
    List<Booking> findByRoomIdAndStatusIn(
            @Param("roomId") Integer roomId, 
            @Param("statuses") List<BookingStatus> statuses);
    
    // Lấy tất cả booking của một khách sạn
    @Query("SELECT b FROM Booking b JOIN b.room r WHERE r.hotel.hotelId = :hotelId")
    Page<Booking> findByHotelId(@Param("hotelId") Integer hotelId, Pageable pageable);
    
    // Lấy booking theo tên phòng hoặc tên người dùng
    @Query("SELECT b FROM Booking b JOIN b.room r JOIN b.user u " +
           "WHERE r.hotel.hotelId = :hotelId " +
           "AND (LOWER(r.roomName) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Booking> findByHotelIdAndRoomNameContainingOrUserFullNameContaining(
            @Param("hotelId") Integer hotelId,
            @Param("query") String query,
            Pageable pageable);
    
    // Lấy booking theo trạng thái
    @Query("SELECT b FROM Booking b JOIN b.room r " +
           "WHERE r.hotel.hotelId = :hotelId AND b.status = :status")
    Page<Booking> findByHotelIdAndStatus(
            @Param("hotelId") Integer hotelId,
            @Param("status") BookingStatus status,
            Pageable pageable);
    
    // Lấy booking theo trạng thái và tìm kiếm
    @Query("SELECT b FROM Booking b JOIN b.room r JOIN b.user u " +
           "WHERE r.hotel.hotelId = :hotelId AND b.status = :status " +
           "AND (LOWER(r.roomName) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Booking> findByHotelIdAndStatusAndRoomNameContainingOrUserFullNameContaining(
            @Param("hotelId") Integer hotelId,
            @Param("status") BookingStatus status,
            @Param("query") String query,
            Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.room.roomId = :roomId AND b.status IN :statuses")
    List<Booking> findByRoomIdAndStatusIn(
            @Param("roomId") Integer roomId, 
            @Param("statuses") List<BookingStatus> statuses);
}
