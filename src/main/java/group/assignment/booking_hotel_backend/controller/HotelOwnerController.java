package group.assignment.booking_hotel_backend.controller;
import group.assignment.booking_hotel_backend.dto.BookingDetailsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import group.assignment.booking_hotel_backend.dto.BookingHotelOwnerDto;
import group.assignment.booking_hotel_backend.dto.BookingResponseDto;
import group.assignment.booking_hotel_backend.dto.BookingStatsDto;
import group.assignment.booking_hotel_backend.dto.CreateRoomRequest;
import group.assignment.booking_hotel_backend.dto.CreateRoomResponse;
import group.assignment.booking_hotel_backend.dto.PutRoomRequest;
import group.assignment.booking_hotel_backend.dto.ReviewStatsDto;
import group.assignment.booking_hotel_backend.dto.RoomDto;
import group.assignment.booking_hotel_backend.dto.RoomResponseDto;
import group.assignment.booking_hotel_backend.dto.RoomResponseHotelOwnerDto;
import group.assignment.booking_hotel_backend.mapper.BookingMapper;
import group.assignment.booking_hotel_backend.mapper.RoomMapper;
import group.assignment.booking_hotel_backend.models.*;
import group.assignment.booking_hotel_backend.repository.ServiceRepository;
import group.assignment.booking_hotel_backend.services.BookingService;
import group.assignment.booking_hotel_backend.services.FilesStorageService;
import group.assignment.booking_hotel_backend.services.HotelService;
import group.assignment.booking_hotel_backend.services.ReviewService;
import group.assignment.booking_hotel_backend.services.RoomImageService;
import group.assignment.booking_hotel_backend.services.RoomService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hotel-owner")
@RequiredArgsConstructor
public class HotelOwnerController {
    private final HotelService hotelService;
    private final RoomService roomService;
    private final ReviewService reviewService;
    private final BookingService bookingService;
    private final ServiceRepository serviceRepository;
    private final RoomImageService roomImageService;
    private final FilesStorageService filesStorageService;
    private final ObjectMapper objectMapper;
   

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
    public ResponseEntity<List<RoomResponseHotelOwnerDto>> getAllRooms(
        @PathVariable Integer hotelId,
        @RequestParam(defaultValue = "") String query,
        @RequestParam(defaultValue = "0") int offset,
        @RequestParam(defaultValue = "10") int limit,
        @RequestParam(defaultValue = "asc") String sort
    ) {
        Sort.Direction direction = sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(direction, "roomName"));

        Page<Room> roomsPage = roomService.findByHotelId(hotelId, query, pageable);
        List<RoomResponseHotelOwnerDto> roomDtos = roomsPage.getContent().stream()
            .map(room -> RoomMapper.mapToRoomResponseHotelOwnerDto(room, new RoomResponseHotelOwnerDto()))
            .collect(Collectors.toList());

        return ResponseEntity.ok(roomDtos);
    }

    @GetMapping("/review/stats/monthly/{hotelId}")
    public ResponseEntity<ReviewStatsDto> getReviewStatsByHotel(@PathVariable Integer hotelId) {
        return ResponseEntity.ok(reviewService.getMonthlyReviewStats(hotelId));
    }

    @GetMapping("/booking/stats/per-day/{hotelId}")
    public ResponseEntity<List<BookingStatsDto>> getBookingStats(
        @PathVariable Integer hotelId,
        @RequestParam(defaultValue = "7") int n
    ) {
        List<BookingStatsDto> stats = bookingService.getBookingStatsLastNDaysForHotel(hotelId, n);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/current-booking/{hotelId}")
    public List<BookingResponseDto> getCurrentBooking(@PathVariable Integer hotelId) {
        List<BookingResponseDto> bookingResponseDtos = new ArrayList<>();
        List<Booking> bookings = bookingService.getCurrentBookingForHotel(hotelId);
        for (Booking b: bookings) {
            bookingResponseDtos.add(BookingMapper.mapToBookingResponseDto(b, new BookingResponseDto()));
        }
        return bookingResponseDtos;
    }

    @GetMapping("/bookings/{hotelId}")
    public ResponseEntity<List<BookingHotelOwnerDto>> getAllBookings(
        @PathVariable Integer hotelId,
        @RequestParam(defaultValue = "") String query,
        @RequestParam(defaultValue = "0") int offset,
        @RequestParam(defaultValue = "10") int limit,
        @RequestParam(defaultValue = "asc") String sort
    ) {
        Sort.Direction direction = sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(direction, "checkIn")); // hoặc "user.fullName" nếu cần

        Page<Booking> bookingsPage = bookingService.getBookingsByHotelId(hotelId, query.toLowerCase(), pageable);

        List<BookingHotelOwnerDto> bookingResponseDtos = bookingsPage.getContent().stream()
            .map(b -> BookingMapper.mapBookingHotelOwnerDto(b, new BookingHotelOwnerDto()))
            .collect(Collectors.toList());

        return ResponseEntity.ok(bookingResponseDtos);
    }



    @PostMapping("/create-with-images")
    public ResponseEntity<?> createRoomWithImages(
            @RequestPart("roomInfo") String roomInfoJson,
            @RequestPart("mainImage") MultipartFile mainImage,
            @RequestPart(value = "extraImages", required = false) List<MultipartFile> extraImages
    ) {
        try {
            // 1. Chuyển JSON thành DTO
            CreateRoomRequest request = objectMapper.readValue(roomInfoJson, CreateRoomRequest.class);

            // 2. Tạo room
            Room room = new Room();
            room.setRoomName(request.getRoomName());
            room.setArea(request.getArea());
            room.setComboPrice2h(request.getComboPrice2h());
            room.setPricePerNight(request.getPricePerNight());
            room.setExtraHourPrice(request.getExtraHourPrice());
            room.setStandardOccupancy(request.getStandardOccupancy());
            room.setMaxOccupancy(request.getMaxOccupancy());
            room.setNumChildrenFree(request.getNumChildrenFree());
            room.setBedNumber(request.getBedNumber());
            room.setExtraAdult(request.getExtraAdult());
            room.setDescription(request.getDescription());

            // 3. Lưu ảnh chính
            if (mainImage != null && !mainImage.isEmpty()) {
                filesStorageService.save(mainImage);
                room.setRoomImg(mainImage.getOriginalFilename());
            }

            // 4. Gán hotel
            Hotel hotel = hotelService.findById(request.getHotelId());
            room.setHotel(hotel);

            // 5. Gán service
            if (request.getServiceIds() != null) {
                List<Service> services = serviceRepository.findAllById(request.getServiceIds());
                room.setServiceList(services);
            }

            Room savedRoom = roomService.save(room);

            List<RoomImage> roomImageList = new ArrayList<>();
            // 6. Lưu danh sách ảnh phụ
            if (extraImages != null) {
                for (MultipartFile file : extraImages) {
                    if (!file.isEmpty()) {
                        filesStorageService.save(file);
                        RoomImage roomImage = RoomImage.builder()
                                .room(savedRoom)
                                .url(file.getOriginalFilename())
                                .build();
                        roomImageService.save(roomImage);
                        roomImageList.add(roomImage);
                    }
                }
            }
            room.setRoomImageList(roomImageList);

            return ResponseEntity.ok(RoomMapper.mapToCreateRoomResponse(savedRoom, new CreateRoomResponse()));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to create room: " + e.getMessage());
        }
    }

    @PutMapping("booking/status/{id}")
    public ResponseEntity<Boolean> updateBookingStatus(
            @PathVariable Integer id,
            @RequestParam BookingStatus status) {
        Booking updated = bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok(true);
    }

    @GetMapping("booking/{id}")
    public ResponseEntity<BookingDetailsDto> getBookingDetails(@PathVariable Integer id) {
        Booking booking = bookingService.findById(id);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(BookingMapper.mapToBookingDetailsDto(booking, new BookingDetailsDto()));
    }

    @GetMapping("/rooms/count/{hotelId}")
    public ResponseEntity<Long> countRoomsByHotelId(@PathVariable Integer hotelId) {
        Long count = roomService.countRoomsByHotelId(hotelId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/bookings/count/{hotelId}")
    public ResponseEntity<Long> countBookingsByHotelId(@PathVariable Integer hotelId) {
        Long count = bookingService.countBookingsByHotelId(hotelId);
        return ResponseEntity.ok(count);
    }

    @PutMapping("/update-with-images")
    public ResponseEntity<?> updateRoomWithImages(
            @RequestPart("roomInfo") String roomInfoJson,
            @RequestPart(value = "mainImage", required = false) MultipartFile mainImage,
            @RequestPart(value = "extraImages", required = false) List<MultipartFile> extraImages
    ) {
        try {
            PutRoomRequest request = objectMapper.readValue(roomInfoJson, PutRoomRequest.class);

            // Tìm phòng cần cập nhật
            Room room = roomService.findById(request.getRoomId());
            if (room == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found");
            }

            // Cập nhật các thông tin cơ bản
            room.setRoomId(request.getRoomId());
            room.setRoomName(request.getRoomName());
            room.setArea(request.getArea());
            room.setComboPrice2h(request.getComboPrice2h());
            room.setPricePerNight(request.getPricePerNight());
            room.setExtraHourPrice(request.getExtraHourPrice());
            room.setStandardOccupancy(request.getStandardOccupancy());
            room.setMaxOccupancy(request.getMaxOccupancy());
            room.setNumChildrenFree(request.getNumChildrenFree());
            room.setBedNumber(request.getBedNumber());
            room.setExtraAdult(request.getExtraAdult());
            room.setDescription(request.getDescription());

            // Cập nhật service
            List<Service> services = serviceRepository.findAllById(request.getServiceDtoList());
            room.setServiceList(services);

            // Cập nhật ảnh chính nếu có
            if (mainImage != null && !mainImage.isEmpty()) {
                filesStorageService.save(mainImage);
                room.setRoomImg(mainImage.getOriginalFilename());
            }

            // Xử lý ảnh phụ
            // 1. Xóa ảnh cũ không còn giữ lại
            List<Integer> keepImageIds = request.getRoomImageUrls();
            List<RoomImage> currentImages = roomImageService.findByRoomRoomId(room.getRoomId());

            for (RoomImage image : currentImages) {
                if (!keepImageIds.contains(image.getImgId())) {
                    filesStorageService.delete(image.getUrl()); // optional: xóa ảnh khỏi disk
                    roomImageService.deleteById(image.getImgId());             // xóa khỏi DB
                }
            }
            
            // 2. Thêm ảnh mới nếu có
            if (extraImages != null) {
                for (MultipartFile file : extraImages) {
                    if (!file.isEmpty()) {
                        String f_tmp = filesStorageService.save(file);
                        RoomImage newImage = RoomImage.builder()
                                .room(room)
                                .url(f_tmp)
                                .build();
                        roomImageService.save(newImage);
                    }
                }
            }

            // Lưu room
            Room updatedRoom = roomService.save(room);

            return ResponseEntity.ok(RoomMapper.mapToCreateRoomResponse(updatedRoom, new CreateRoomResponse()));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update room: " + e.getMessage());
        }
    }

}
