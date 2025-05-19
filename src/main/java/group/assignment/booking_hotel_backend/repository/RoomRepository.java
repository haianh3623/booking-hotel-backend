package group.assignment.booking_hotel_backend.repository;
import group.assignment.booking_hotel_backend.dto.RoomDto;
import group.assignment.booking_hotel_backend.dto.RoomSearchListDto;
import group.assignment.booking_hotel_backend.models.Room;

import java.util.List;

import group.assignment.booking_hotel_backend.models.SearchRequest;
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
    @Query("SELECT r FROM Room r WHERE r.hotel.hotelId = :hotelId")
    List<Room> findByHotelId(@Param("hotelId") Integer hotelId);

    @Query("SELECT COUNT(r) FROM Room r WHERE r.hotel.hotelId = :hotelId")
    Long countByHotelId(@Param("hotelId") Integer hotelId);
    @Query("SELECT new group.assignment.booking_hotel_backend.dto.RoomSearchListDto(" +
            "r.roomId, r.roomName, h.hotelName, r.area, r.standardOccupancy, " +
            "r.roomImg, r.bedNumber, 0.0, 0, r.maxOccupancy, r.numChildrenFree) " +  // rating và reviewCount tạm hardcode
            "FROM Room r " +
            "JOIN r.hotel h " )
    List<RoomSearchListDto> findAllRoomList();

    @Query("SELECT new group.assignment.booking_hotel_backend.dto.RoomSearchListDto(" +
            "r.roomId, r.roomName, h.hotelName, r.area, r.standardOccupancy, " +
            "r.roomImg, r.bedNumber, 0.0, 0," +
            "r.maxOccupancy, r.numChildrenFree) " +  // rating và reviewCount tạm hardcode
            "FROM Room r " +
            "JOIN r.hotel h " +
            "JOIN h.address a " +  // join với bảng Address
            "WHERE (:keyword IS NULL OR " +
            "LOWER(h.hotelName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(r.roomName) LIKE LOWER(CONCAT('%', :keyword, '%'))"+
            "OR LOWER(a.city) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(a.district) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(a.ward) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(a.specificAddress) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<RoomSearchListDto> searchRoomListByKeyword(@Param("keyword") String keyword);
    List<Room> findAllByHotel_HotelId(Integer hotelId);
}
