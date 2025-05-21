package group.assignment.booking_hotel_backend.repository;

import group.assignment.booking_hotel_backend.dto.AddressDto;
import group.assignment.booking_hotel_backend.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    @Query("SELECT DISTINCT a.district FROM Address a WHERE a.city = :city")
    List<String> findDistrictByCity(@Param("city") String city);
    @Query("SELECT DISTINCT a.city FROM Address a")
    List<String> findAllCity();
    @Query("SELECT NEW group.assignment.booking_hotel_backend.dto.AddressDto(a.city, a.district, a.ward, a.specificAddress) " +
            "FROM Hotel h JOIN h.address a WHERE h.hotelName = :hotelName")
    List<AddressDto> findAddressByHotelName(@Param("hotelName") String hotelName);
}
