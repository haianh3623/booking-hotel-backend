package group.assignment.booking_hotel_backend.services.impl;

import group.assignment.booking_hotel_backend.models.Hotel;
import group.assignment.booking_hotel_backend.repository.HotelRepository;
import group.assignment.booking_hotel_backend.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public void save(Hotel hotel) {
        hotelRepository.save(hotel);
    }

    @Override
    public List<Hotel> findAll() {
        return hotelRepository.findAll();
    }

    @Override
    public Hotel findById(Integer id) {
        Optional<Hotel> optionalHotel = hotelRepository.findById(id);
        return optionalHotel.orElse(null);
    }

    @Override
    public void deleteById(Integer id) {
        hotelRepository.deleteById(id);
    }

    @Override
    public List<Hotel> findByUserId(Integer userId) {
        return hotelRepository.findByUserUserId(userId);
    }

    @Override
    public List<String> getAllCities() {
        return hotelRepository.findDistinctCities();
    }

    @Override
    public List<String> getAllDistricts() {
        return hotelRepository.findDistinctDistricts();
    }
}