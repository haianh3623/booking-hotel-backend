package group.assignment.booking_hotel_backend.controller;

import group.assignment.booking_hotel_backend.dto.BookingSearchResponse;
import group.assignment.booking_hotel_backend.dto.BookingServiceResponse;
import group.assignment.booking_hotel_backend.mapper.ServiceMapper;
import group.assignment.booking_hotel_backend.services.RoomService;
import group.assignment.booking_hotel_backend.services.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/service")
@RequiredArgsConstructor
public class ServiceController {
    private final RoomService roomService;
    private final ServiceService serviceService;

    @GetMapping("")
    public ResponseEntity<List<BookingServiceResponse>> getAllService() {
        try {
            List<BookingServiceResponse> serviceList = serviceService.findAll().stream()
                    .map(service -> ServiceMapper.mapToBookingServiceResponse(service, new BookingServiceResponse()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(serviceList);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
