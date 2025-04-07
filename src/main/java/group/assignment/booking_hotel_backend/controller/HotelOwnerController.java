package group.assignment.booking_hotel_backend.controller;

import group.assignment.booking_hotel_backend.dto.HotelDto;
import group.assignment.booking_hotel_backend.mapper.HotelMapper;
import group.assignment.booking_hotel_backend.models.Hotel;
import group.assignment.booking_hotel_backend.services.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotel-owner")
@RequiredArgsConstructor
public class HotelOwnerController {
    private final HotelService hotelService;

    @PreAuthorize("hasRole('HOTEL_OWNER')")
    @GetMapping("/")
    public ResponseEntity<String> getHotelOwnerHome() {
        return ResponseEntity.ok("Welcome to hotel owner home page");
    }

    @GetMapping("/hotels/{userId}")
    public List<Hotel> getHotelsByUser(@PathVariable Integer userId) {
        return hotelService.findByUserId(userId);
    }

    @DeleteMapping("/hotel/{hotelId}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Integer hotelId) {
        hotelService.deleteById(hotelId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable Integer hotelId) {
        return ResponseEntity.ok(hotelService.findById(hotelId));
    }
}
