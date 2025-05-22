package group.assignment.booking_hotel_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import group.assignment.booking_hotel_backend.dto.*;
import group.assignment.booking_hotel_backend.mapper.RoomMapper;
import group.assignment.booking_hotel_backend.models.*;
import group.assignment.booking_hotel_backend.repository.*;
import group.assignment.booking_hotel_backend.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final HotelService hotelService;
    private final ServiceRepository serviceRepository;
    private final RoomImageService roomImageService;
    private final FilesStorageService filesStorageService;
    private final ObjectMapper objectMapper;
    private final BookingService bookingService;

    @GetMapping("/top-rated")
    public ResponseEntity<?> getTopRatedRooms() {
        List<RoomResponseDto> topRooms = roomService.getTopRatedRooms(5);
        return ResponseEntity.ok(topRooms);
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

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<?> getRoomByBookingId(@PathVariable Integer bookingId) {
        try {
            Booking booking = bookingService.findById(bookingId);
            if (booking == null || booking.getRoom() == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(RoomMapper.mapToRoomDto(booking.getRoom(), new RoomResponseDto()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving room: " + e.getMessage());
        }
    }

//    @GetMapping("/search")
//    public ResponseEntity<?> searchRoomSearchList(@RequestParam(value = "keyword", required = false) String keyword) {
//        try {
//            List<RoomSearchListDto> roomList = roomService.findRoomByKeyword(keyword, null);
//            return ResponseEntity.ok(roomList);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Error searching rooms: " + e.getMessage());
//        }
//    }

    @PostMapping("/search")
    public ResponseEntity<?> searchRoomListBySearchRequest(@RequestBody SearchRequest req){
        try {
            List<RoomSearchListDto> roomList = roomService.findRoomBySearchRequest(req, null);
            return ResponseEntity.ok(roomList);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error searching rooms: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDto> getRoom(@PathVariable Integer id) {
        return ResponseEntity.ok(RoomMapper.mapToRoomDto(roomService.findById(id), new RoomResponseDto()));
    }

    @GetMapping("")
    public ResponseEntity<List<RoomResponseDto>> getAllRooms() {
        List<RoomResponseDto> roomList = new ArrayList<>();
        for (Room room : roomService.findAll()) {
            roomList.add(RoomMapper.mapToRoomDto(room, new RoomResponseDto()));
        }
        return ResponseEntity.ok(roomList);
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<RoomResponseDto>> getAllRoomsByHotel(@PathVariable Integer hotelId) {
        List<RoomResponseDto> roomList = new ArrayList<>();
        for (Room room : roomService.findAllByHotelId(hotelId)) {
            roomList.add(RoomMapper.mapToRoomDto(room, new RoomResponseDto()));
        }
        return ResponseEntity.ok(roomList);
    }

    @GetMapping("/room-details/{roomId}")
    public ResponseEntity<?> getRoomDetails(@PathVariable Integer roomId) {
        try {
            RoomDetailsDto roomDetails = roomService.getRoomDetails(roomId);
            return ResponseEntity.ok(roomDetails);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving room details: " + e.getMessage());
        }
    }
}


