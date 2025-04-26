package group.assignment.booking_hotel_backend.controller;

import group.assignment.booking_hotel_backend.dto.HotelDto;
import group.assignment.booking_hotel_backend.dto.RoomDto;
import group.assignment.booking_hotel_backend.dto.UserDto;
import group.assignment.booking_hotel_backend.dto.UserResponseDto;
import group.assignment.booking_hotel_backend.mapper.HotelMapper;
import group.assignment.booking_hotel_backend.mapper.UserMapper;
import group.assignment.booking_hotel_backend.models.Hotel;
import group.assignment.booking_hotel_backend.models.Role;
import group.assignment.booking_hotel_backend.models.Room;
import group.assignment.booking_hotel_backend.models.User;
import group.assignment.booking_hotel_backend.services.HotelService;
import group.assignment.booking_hotel_backend.services.RoomService;
import group.assignment.booking_hotel_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/hotel-owner")
@RequiredArgsConstructor
public class HotelOwnerController {
    private final HotelService hotelService;
    private final RoomService roomService;
    private final UserService userService;

    @PreAuthorize("hasRole('HOTEL_OWNER')")
    @GetMapping("/")
    public ResponseEntity<String> getHotelOwnerHome() {
        return ResponseEntity.ok("Welcome to hotel owner home page");
    }

    @GetMapping("/hotels/{userId}")
    public ResponseEntity<List<HotelDto>> getHotelsByUser(@PathVariable Integer userId) {
        List<HotelDto> hotels = new ArrayList<>();
        for (Hotel hotel : hotelService.findByUserId(userId)) {
            hotels.add(HotelMapper.mapToHotelDto(hotel, new HotelDto()));
        }
        return ResponseEntity.ok(hotels);
    }

    @DeleteMapping("/hotel/{hotelId}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Integer hotelId) {
        hotelService.deleteById(hotelId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Integer hotelId) {
        return ResponseEntity.ok(HotelMapper.mapToHotelDto(hotelService.findById(hotelId), new HotelDto()));
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

    @GetMapping("/hotels")
    public ResponseEntity<List<HotelDto>> getHotelsByRoleOwner() {
        List<HotelDto> hotels = new ArrayList<>();
        for (Hotel hotel : hotelService.findHotelsByRoleOwner("ROLE_HOTEL_OWNER")) {
            hotels.add(HotelMapper.mapToHotelDto(hotel, new HotelDto()));
        }
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserResponseDto>> getUserOwner() {
        List<User> userList = userService.findByRoleList_Name("ROLE_HOTEL_OWNER");
        List<UserResponseDto> userDtos = new ArrayList<>();
        for (User user : userList) {
            userDtos.add(UserMapper.mapToUserDto(user, new UserResponseDto()));
        }
        return ResponseEntity.ok(userDtos);
    }

//    @GetMapping("/hotels/{userId}")
//    public ResponseEntity<List<HotelDto>> getHotelsByIdOwner(@PathVariable Integer userId) {
//        List<HotelDto> hotels = new ArrayList<>();
//        for (Hotel hotel : hotelService.findHotelsByIdOwner(userId)) {
//            hotels.add(HotelMapper.mapToHotelDto(hotel, new HotelDto()));
//        }
//        return ResponseEntity.ok(hotels);
//    }
}
