package group.assignment.booking_hotel_backend.controller;

import group.assignment.booking_hotel_backend.services.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}