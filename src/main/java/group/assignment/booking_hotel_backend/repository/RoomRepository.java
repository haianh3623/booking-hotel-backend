package group.assignment.booking_hotel_backend.repository;
import group.assignment.booking_hotel_backend.dto.RoomSearchListDto;
import group.assignment.booking_hotel_backend.models.Room;

import java.util.List;

import group.assignment.booking_hotel_backend.models.SearchRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    @Query("SELECT r FROM Room r WHERE r.hotel.hotelId = :hotelId")
    List<Room> findByHotelId(@Param("hotelId") Integer hotelId);

    @Query("SELECT new group.assignment.booking_hotel_backend.dto.RoomSearchListDto(" +
            "r.roomId, r.roomName, h.hotelName, r.area, r.standardOccupancy, " +
            "r.roomImg, r.bedNumber, 0.0, 0) " +  // rating và reviewCount tạm hardcode
            "FROM Room r " +
            "JOIN r.hotel h " )
    List<RoomSearchListDto> findAllRoomList();

    @Query("SELECT new group.assignment.booking_hotel_backend.dto.RoomSearchListDto(" +
            "r.roomId, r.roomName, h.hotelName, r.area, r.standardOccupancy, " +
            "r.roomImg, r.bedNumber, 0.0, 0) " +  // rating và reviewCount tạm hardcode
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

//    @Query("""
//        SELECT new group.assignment.booking_hotel_backend.dto.RoomSearchListDto(
//            r.roomId, r.name, h.name, r.area, r.standardOccupancy,
//            r.roomImg, r.bedNumber, AVG(r.rating), COUNT(r.reviewCount))
//        FROM Room r
//        JOIN r.hotel h
//        JOIN h.address a
//        LEFT JOIN r.services s
//        WHERE
//            (:#{#req.keyword} IS NULL OR r.name LIKE %:#{#req.keyword}% OR h.name LIKE %:#{#req.keyword}%)
//            AND (:#{#req.city} IS NULL OR a.city = :#{#req.city})
//            AND (:#{#req.district} IS NULL OR a.district = :#{#req.district})
//            AND (:#{#req.numOfAdult} IS NULL OR r.numOfAdult >= :#{#req.numOfAdult})
//            AND (:#{#req.numOfChild} IS NULL OR r.numOfChild >= :#{#req.numOfChild})
//            AND (:#{#req.numOfBed} IS NULL OR r.numOfBed >= :#{#req.numOfBed})
//            AND (
//                :#{#req.services == null || #req.services.isEmpty()} = TRUE
//                OR s.name IN :#{#req.services}
//            )
//        GROUP BY r
//        HAVING COUNT(DISTINCT s.name) >= :#{#req.services == null ? 0 : #req.services.size()}
//        """)
//    List<RoomSearchListDto> searchRoomListBySearchRequest(@Param("req") SearchRequest req);
}
