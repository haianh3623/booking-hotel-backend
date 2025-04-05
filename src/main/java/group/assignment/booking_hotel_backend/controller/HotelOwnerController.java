package group.assignment.booking_hotel_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hotel-owner")
public class HotelOwnerController {
    @PreAuthorize("hasRole('HOTEL_OWNER')")
    @GetMapping("/")
    public ResponseEntity<String> getHotelOwnerHome() {
        return ResponseEntity.ok("Welcome to hotel owner home page");
    }
}
