package group.assignment.booking_hotel_backend.repository;

import group.assignment.booking_hotel_backend.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {
    List<Hotel> findByUserUserId(Integer userId);
    List<Hotel> findByAddressCityAndAddressDistrict(String city, String district);

    @Query("SELECT DISTINCT a.city FROM Hotel h JOIN h.address a")
    List<String> findDistinctCities();

    @Query("SELECT DISTINCT a.district FROM Hotel h JOIN h.address a")
    List<String> findDistinctDistricts();

    
}
