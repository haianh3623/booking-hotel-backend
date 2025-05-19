package group.assignment.booking_hotel_backend.services;

import group.assignment.booking_hotel_backend.models.Hotel;

import java.util.List;

public interface HotelService {
    Hotel save(Hotel hotel);
    List<Hotel> findAll();
    Hotel findById(Integer id);
    void deleteById(Integer id);
    List<Hotel> findByUserId(Integer userId);
    List<String> getAllCities();
    List<String> getAllDistricts();
    Long count();
    List<Hotel> findHotelsByRoleOwner(String role);
    List<Hotel> findHotelsByIdOwner(Integer id);
}