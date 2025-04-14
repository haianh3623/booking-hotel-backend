package group.assignment.booking_hotel_backend.controller;

import group.assignment.booking_hotel_backend.dto.BookingRequestDto;
import group.assignment.booking_hotel_backend.dto.BookingResponseDto;
import group.assignment.booking_hotel_backend.dto.BookingSearchRequest;
import group.assignment.booking_hotel_backend.dto.BookingSearchResponse;
import group.assignment.booking_hotel_backend.mapper.BookingMapper;
import group.assignment.booking_hotel_backend.models.Booking;
import group.assignment.booking_hotel_backend.models.BookingStatus;
import group.assignment.booking_hotel_backend.services.BookingService;
import group.assignment.booking_hotel_backend.services.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/booking")
public class BookingController {
    private final RoomService roomService;
    private final BookingService bookingService;

    @PostMapping("/search")
    public ResponseEntity<List<BookingSearchResponse>> searchAvailableRooms(@RequestBody BookingSearchRequest request) {
        try {
            List<BookingSearchResponse> availableRooms = bookingService.searchAvailableRooms(request);
            return ResponseEntity.ok(availableRooms);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<BookingResponseDto> create(@RequestBody BookingRequestDto dto) {
        return ResponseEntity.ok(bookingService.createBooking(dto));
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDto>> getAll() {
        List<BookingResponseDto> responses = new ArrayList<>();
        for (Booking booking : bookingService.findAll()) {
            BookingResponseDto response = BookingMapper.mapToBookingResponseDto(booking, new BookingResponseDto());
            responses.add(response);
        }
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(BookingMapper.mapToBookingResponseDto(bookingService.findById(id), new BookingResponseDto()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponseDto> update(@PathVariable Integer id, @RequestBody BookingRequestDto dto) {
        return ResponseEntity.ok(BookingMapper.mapToBookingResponseDto(bookingService.updateBooking(id, dto), new BookingResponseDto()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        bookingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<BookingResponseDto> updateStatus(
            @PathVariable Integer id,
            @RequestParam BookingStatus status) {
        Booking updated = bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok(BookingMapper.mapToBookingResponseDto(updated, new BookingResponseDto()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByUserId(@PathVariable Integer userId) {
        try {
            List<Booking> bookings = bookingService.findByUserId(userId);
            List<BookingResponseDto> bookingDtos = new ArrayList<>();
            for (Booking booking : bookings) {
                BookingResponseDto responseDto = BookingMapper.mapToBookingResponseDto(booking, new BookingResponseDto());
                bookingDtos.add(responseDto);
            }
            return ResponseEntity.ok(bookingDtos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
