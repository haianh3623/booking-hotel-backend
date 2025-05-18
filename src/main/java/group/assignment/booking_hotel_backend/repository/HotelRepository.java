package group.assignment.booking_hotel_backend.repository;

import group.assignment.booking_hotel_backend.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {
    List<Hotel> findByUserUserId(Integer userId);
    List<Hotel> findByAddressCityAndAddressDistrict(String city, String district);

    List<Hotel> findByHotelNameContainingIgnoreCase(String hotelName);

    @Query("SELECT DISTINCT a.city FROM Hotel h JOIN h.address a")
    List<String> findDistinctCities();

    @Query("SELECT DISTINCT a.district FROM Hotel h JOIN h.address a")
    List<String> findDistinctDistricts();

    @Query("SELECT a.city FROM Hotel h JOIN h.address a WHERE h.hotelName = :hotelName")
    List<String> findCityByHotelName(@Param("hotelName") String hotelName);

    @Query("SELECT a.district FROM Hotel h JOIN h.address a WHERE h.hotelName = :hotelName")
    List<String> findDistrictByHotelName(@Param("hotelName") String hotelName);

    @Query("SELECT h.hotelName FROM Room r JOIN r.hotel h WHERE r.roomId = :roomId")
    String findHotelNameByRoomId(@Param("roomId") Integer roomId);

    @Query("SELECT h.name FROM Hotel h WHERE LOWER(h.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<String> findHotelNamesByKeyword(@Param("keyword") String keyword);
}
