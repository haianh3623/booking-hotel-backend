package group.assignment.booking_hotel_backend.controller;

import group.assignment.booking_hotel_backend.dto.HotelDto;
import group.assignment.booking_hotel_backend.dto.RoomDto;
import group.assignment.booking_hotel_backend.mapper.HotelMapper;
import group.assignment.booking_hotel_backend.models.Hotel;
import group.assignment.booking_hotel_backend.models.Room;
import group.assignment.booking_hotel_backend.services.HotelService;
import group.assignment.booking_hotel_backend.services.RoomService;
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
    private final RoomService roomService;

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


    @PostMapping("/room")
    public ResponseEntity<Room> createRoom(@RequestBody RoomDto roomDto) {
        return ResponseEntity.ok(roomService.save(roomDto));
    }

    @PutMapping("/room/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Integer id, @RequestBody RoomDto roomDto) {
        return ResponseEntity.ok(roomService.update(id, roomDto));
    }


    @DeleteMapping("/room/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Integer id) {
        return ResponseEntity.ok(roomService.deleteById(id));
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<Room> getRoom(@PathVariable Integer id) {
        return ResponseEntity.ok(roomService.findById(id));
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomService.findAll());
    }
}
