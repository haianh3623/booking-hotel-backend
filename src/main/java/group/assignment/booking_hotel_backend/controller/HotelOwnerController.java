package group.assignment.booking_hotel_backend.controller;
import group.assignment.booking_hotel_backend.dto.BookingResponseDto;
import group.assignment.booking_hotel_backend.dto.BookingStatsDto;
import group.assignment.booking_hotel_backend.dto.ReviewStatsDto;
import group.assignment.booking_hotel_backend.dto.RoomDto;
import group.assignment.booking_hotel_backend.dto.RoomResponseDto;
import group.assignment.booking_hotel_backend.mapper.BookingMapper;
import group.assignment.booking_hotel_backend.mapper.RoomMapper;
import group.assignment.booking_hotel_backend.models.Booking;
import group.assignment.booking_hotel_backend.models.Hotel;
import group.assignment.booking_hotel_backend.models.Room;
import group.assignment.booking_hotel_backend.services.BookingService;
import group.assignment.booking_hotel_backend.services.HotelService;
import group.assignment.booking_hotel_backend.services.ReviewService;
import group.assignment.booking_hotel_backend.services.RoomService;
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
    private final ReviewService reviewService;
    private final BookingService bookingService;

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

    @GetMapping("/rooms/{hotelId}")
    public ResponseEntity<List<RoomResponseDto>> getAllRooms(@PathVariable Integer hotelId) {
        List<Room> rooms = roomService.findByHotelId(hotelId);
        List<RoomResponseDto> roomDtos = new ArrayList<>();
        for (Room room : rooms) {
            roomDtos.add(RoomMapper.mapToRoomDto(room, new RoomResponseDto()));
        }
        return ResponseEntity.ok(roomDtos);
    }

    @GetMapping("/review/stats/monthly")
    public ResponseEntity<ReviewStatsDto> getReviewStatsByHotel(@RequestParam Integer hotelId) {
        return ResponseEntity.ok(reviewService.getMonthlyReviewStats(hotelId));
    }

    @GetMapping("/booking/stats/per-day")
    public ResponseEntity<List<BookingStatsDto>> getBookingStats(
        @RequestParam Integer hotelId,
        @RequestParam(defaultValue = "7") int n
    ) {
        List<BookingStatsDto> stats = bookingService.getBookingStatsLastNDaysForHotel(hotelId, n);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/booking/{hotelId}")
    public List<BookingResponseDto> getCurrentBooking(@PathVariable Integer hotelId) {
        List<BookingResponseDto> bookingResponseDtos = new ArrayList<>();
        List<Booking> bookings = bookingService.getCurrentBookingForHotel(hotelId);
        for (Booking b: bookings) {
            bookingResponseDtos.add(BookingMapper.mapToBookingResponseDto(b, new BookingResponseDto()));
        }
        return bookingResponseDtos;
    }
}
