package group.assignment.booking_hotel_backend.controller;

import group.assignment.booking_hotel_backend.dto.AddressDto;
import group.assignment.booking_hotel_backend.dto.HotelDto;
import group.assignment.booking_hotel_backend.dto.HotelRequestDto;
import group.assignment.booking_hotel_backend.mapper.AddressMapper;
import group.assignment.booking_hotel_backend.mapper.HotelMapper;
import group.assignment.booking_hotel_backend.models.Address;
import group.assignment.booking_hotel_backend.models.Hotel;
import group.assignment.booking_hotel_backend.services.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/hotel")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/cities")
    public ResponseEntity<List<String>> getAllCities() {
        return ResponseEntity.ok(hotelService.getAllCities());
    }
    @GetMapping("/districts")
    public ResponseEntity<List<String>> getAllDistricts() {
        return ResponseEntity.ok(hotelService.getAllDistricts());
    }

    @GetMapping
    public ResponseEntity<List<HotelDto>> getAllHotels() {
        List<HotelDto> hotels = new ArrayList<>();
        for (Hotel hotel : hotelService.findAll()) {
            hotels.add(HotelMapper.mapToHotelDto(hotel, new HotelDto()));
        }
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Integer id) {
        Hotel hotel = hotelService.findById(id);
        if (hotel != null) {
            return ResponseEntity.ok(HotelMapper.mapToHotelDto(hotel, new HotelDto()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<HotelDto> updateHotel(@PathVariable Integer id, @RequestBody HotelDto hotel) {
        Hotel existingHotel = hotelService.findById(id);
        if (existingHotel != null) {
            existingHotel.setHotelName(hotel.getHotelName());
            existingHotel.setAddress(AddressMapper.mapToAddress(hotel.getAddress(), new Address()));
            return ResponseEntity.ok(HotelMapper.mapToHotelDto(hotelService.save(existingHotel), new HotelDto()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Integer id) {
        Hotel existingHotel = hotelService.findById(id);
        if (existingHotel != null) {
            hotelService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HotelDto>> getHotelsByUserId(@PathVariable Integer userId) {
        List<HotelDto> hotels = new ArrayList<>();
        for (Hotel hotel : hotelService.findByUserId(userId)) {
            hotels.add(HotelMapper.mapToHotelDto(hotel, new HotelDto()));
        }
        return ResponseEntity.ok(hotels);
    }
}