package group.assignment.booking_hotel_backend.controller;

import group.assignment.booking_hotel_backend.dto.BookingDetailsDto;
import group.assignment.booking_hotel_backend.dto.BookingHotelOwnerDto;
import group.assignment.booking_hotel_backend.dto.BookingResponseDto;
import group.assignment.booking_hotel_backend.dto.BookingStatsDto;
import group.assignment.booking_hotel_backend.dto.CreateRoomRequest;
import group.assignment.booking_hotel_backend.dto.CreateRoomResponse;
import group.assignment.booking_hotel_backend.dto.PutRoomRequest;
import group.assignment.booking_hotel_backend.dto.ReviewResponseForOwnerDto;
import group.assignment.booking_hotel_backend.dto.ReviewStatsDto;
import group.assignment.booking_hotel_backend.dto.RoomResponseHotelOwnerDto;
import group.assignment.booking_hotel_backend.mapper.BookingMapper;
import group.assignment.booking_hotel_backend.mapper.HotelMapper;
import group.assignment.booking_hotel_backend.mapper.RoomMapper;
import group.assignment.booking_hotel_backend.models.Booking;
import group.assignment.booking_hotel_backend.models.BookingStatus;
import group.assignment.booking_hotel_backend.models.Hotel;
import group.assignment.booking_hotel_backend.models.Review;
import group.assignment.booking_hotel_backend.models.Room;
import group.assignment.booking_hotel_backend.models.RoomImage;
import group.assignment.booking_hotel_backend.models.Service;
import group.assignment.booking_hotel_backend.repository.ServiceRepository;
import group.assignment.booking_hotel_backend.services.BookingService;
import group.assignment.booking_hotel_backend.services.FilesStorageService;
import group.assignment.booking_hotel_backend.services.HotelService;
import group.assignment.booking_hotel_backend.services.ReviewService;
import group.assignment.booking_hotel_backend.services.RoomImageService;
import group.assignment.booking_hotel_backend.services.RoomService;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import group.assignment.booking_hotel_backend.dto.HotelDto;

/**
 * Controller for hotel owner operations including room, booking and statistics management
 */
@RestController
@RequestMapping("/api/hotel-owner")
@RequiredArgsConstructor
public class HotelOwnerController {
    private static final Logger logger = LoggerFactory.getLogger(HotelOwnerController.class);
    
    private final HotelService hotelService;
    private final RoomService roomService;
    private final ReviewService reviewService;
    private final BookingService bookingService;
    private final ServiceRepository serviceRepository;
    private final RoomImageService roomImageService;
    private final FilesStorageService filesStorageService;
    private final ObjectMapper objectMapper;

    /**
     * Welcome endpoint for hotel owner home page
     * @return Welcome message
     */
    @PreAuthorize("hasRole('HOTEL_OWNER')")
    @GetMapping("/")
    public ResponseEntity<String> getHotelOwnerHome() {
        return ResponseEntity.ok("Welcome to hotel owner home page");
    }

    /**
     * Get hotels by user ID
     * @param userId ID of the user
     * @return List of hotels owned by the user
     */
    @GetMapping("/hotels/{userId}")
    public ResponseEntity<List<HotelDto>> getHotelsByUser(@PathVariable Integer userId) {
        List<Hotel> hotels = hotelService.findByUserId(userId);
        List<HotelDto> hotelDtos = hotels.stream()
                .map(hotel -> HotelMapper.mapToHotelDto(hotel, new HotelDto()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(hotelDtos);
    }
    /**
     * Get hotel by ID
     * @param hotelId ID of the hotel
     * @return Hotel details
     */
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Integer hotelId) {
        Hotel hotel = hotelService.findById(hotelId);
        if (hotel == null) {
            return ResponseEntity.notFound().build();
        }
    
    HotelDto hotelDto = HotelMapper.mapToHotelDto(hotel, new HotelDto());
    
    return ResponseEntity.ok(hotelDto);
}

    /**
     * Delete hotel by ID
     * @param hotelId ID of the hotel to delete
     * @return No content response
     */
    @DeleteMapping("/hotel/{hotelId}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Integer hotelId) {
        hotelService.deleteById(hotelId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get review statistics for a hotel
     * @param hotelId ID of the hotel
     * @return Review statistics
     */
    @GetMapping("/review/stats/monthly/{hotelId}")
    public ResponseEntity<ReviewStatsDto> getReviewStatsByHotel(@PathVariable Integer hotelId) {
        return ResponseEntity.ok(reviewService.getMonthlyReviewStats(hotelId));
    }

    /**
     * Get booking statistics for a hotel
     * @param hotelId ID of the hotel
     * @param n Number of days to retrieve statistics for (default 7)
     * @return List of booking statistics by day
     */
    @GetMapping("/booking/stats/per-day/{hotelId}")
    public ResponseEntity<List<BookingStatsDto>> getBookingStats(
            @PathVariable Integer hotelId,
            @RequestParam(defaultValue = "7") int n) {
        List<BookingStatsDto> stats = bookingService.getBookingStatsLastNDaysForHotel(hotelId, n);
        return ResponseEntity.ok(stats);
    }

    /**
     * Get current bookings for a hotel
     * @param hotelId ID of the hotel
     * @return List of current bookings
     */
    @GetMapping("/current-booking/{hotelId}")
    public ResponseEntity<List<BookingResponseDto>> getCurrentBooking(@PathVariable Integer hotelId) {
        List<BookingResponseDto> bookingResponseDtos = bookingService.getCurrentBookingForHotel(hotelId)
                .stream()
                .map(booking -> BookingMapper.mapToBookingResponseDto(booking, new BookingResponseDto()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(bookingResponseDtos);
    }

    /**
     * Get all rooms for a hotel with pagination and filtering
     * @param hotelId ID of the hotel
     * @param query Search query (default empty)
     * @param offset Pagination offset (default 0)
     * @param limit Pagination limit (default 10)
     * @param order Sort order (default "asc")
     * @return List of rooms
     */
    @GetMapping("/rooms/{hotelId}")
    public ResponseEntity<List<RoomResponseHotelOwnerDto>> getAllRooms(
            @PathVariable Integer hotelId,
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "asc") String order) {
        
        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(direction, "roomName"));

        Page<Room> roomsPage = roomService.findByHotelId(hotelId, query, pageable);
        List<RoomResponseHotelOwnerDto> roomDtos = roomsPage.getContent().stream()
                .map(room -> RoomMapper.mapToRoomResponseHotelOwnerDto(room, new RoomResponseHotelOwnerDto()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(roomDtos);
    }

    /**
     * Get all bookings for a hotel with pagination and filtering
     * @param hotelId ID of the hotel
     * @param query Search query (default empty)
     * @param offset Pagination offset (default 0)
     * @param limit Pagination limit (default 10)
     * @param order Sort order (default "asc")
     * @return List of bookings
     */
    @GetMapping("/bookings/{hotelId}")
    public ResponseEntity<List<BookingHotelOwnerDto>> getAllBookings(
            @PathVariable Integer hotelId,
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "asc") String order) {
        
        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(direction, "checkIn"));

        Page<Booking> bookingsPage = bookingService.getBookingsByHotelId(hotelId, query.toLowerCase(), pageable);
        List<BookingHotelOwnerDto> bookingResponseDtos = bookingsPage.getContent().stream()
                .map(booking -> BookingMapper.mapBookingHotelOwnerDto(booking, new BookingHotelOwnerDto()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(bookingResponseDtos);
    }

    /**
     * Create a new room with images
     * @param roomInfoJson Room information in JSON format
     * @param mainImage Main image of the room
     * @param extraImages Additional images of the room
     * @return Created room response
     */
    @PostMapping(value = "/create-with-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createRoomWithImages(
            @RequestPart("roomInfo") String roomInfoJson,
            @RequestPart("mainImage") MultipartFile mainImage,
            @RequestPart(value = "extraImages", required = false) List<MultipartFile> extraImages) {
        try {
        logger.info("Creating room with info: {}", roomInfoJson);
        
        // Parse JSON to DTO
        CreateRoomRequest request = objectMapper.readValue(roomInfoJson, CreateRoomRequest.class);

        // Create room object
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

        // Save main image
        if (mainImage != null && !mainImage.isEmpty()) {
            String filename = filesStorageService.save(mainImage);
            room.setRoomImg(filename);
        }

        // Set hotel
        Hotel hotel = hotelService.findById(request.getHotelId());
        room.setHotel(hotel);

        // Set services - improved handling of service IDs
        if (request.getServiceIds() != null && !request.getServiceIds().isEmpty()) {
            logger.info("Setting services: {}", request.getServiceIds());
            List<Service> services = serviceRepository.findAllById(request.getServiceIds());
            if (services.size() != request.getServiceIds().size()) {
                logger.warn("Some service IDs were not found. Found {} out of {}", 
                    services.size(), request.getServiceIds().size());
            }
            room.setServiceList(services);
        } else {
            logger.info("No services provided for the room");
            room.setServiceList(new ArrayList<>());
        }

        // Save room
        Room savedRoom = roomService.save(room);

        // Save extra images
        List<RoomImage> roomImageList = new ArrayList<>();
        if (extraImages != null && !extraImages.isEmpty()) {
            for (MultipartFile file : extraImages) {
                if (!file.isEmpty()) {
                    String filename = filesStorageService.save(file);
                    RoomImage roomImage = RoomImage.builder()
                            .room(savedRoom)
                            .url(filename)
                            .build();
                    roomImageService.save(roomImage);
                    roomImageList.add(roomImage);
                }
            }
        }
        savedRoom.setRoomImageList(roomImageList);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RoomMapper.mapToCreateRoomResponse(savedRoom, new CreateRoomResponse()));

    } catch (Exception e) {
        logger.error("Error creating room with images", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Failed to create room: " + e.getMessage());
    }
}

    /**
     * Update a room with images
     * @param roomInfoJson Room information in JSON format
     * @param mainImage Main image of the room (optional)
     * @param extraImages Additional images of the room (optional)
     * @return Updated room response
     */
    @PutMapping(value = "/update-with-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateRoomWithImages(
            @RequestPart("roomInfo") String roomInfoJson,
            @RequestPart(value = "mainImage", required = false) MultipartFile mainImage,
            @RequestPart(value = "extraImages", required = false) List<MultipartFile> extraImages) {
        try {
            logger.info("Updating room with info: {}", roomInfoJson);
            
            PutRoomRequest request = objectMapper.readValue(roomInfoJson, PutRoomRequest.class);

            // Find room to update
            Room room = roomService.findById(request.getRoomId());
            if (room == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found");
            }

            // Update basic information
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

            // Update services - improved handling of service IDs
            if (request.getServiceIds() != null && !request.getServiceIds().isEmpty()) {
                logger.info("Updating services: {}", request.getServiceIds());
                List<Service> services = serviceRepository.findAllById(request.getServiceIds());
                if (services.size() != request.getServiceIds().size()) {
                    logger.warn("Some service IDs were not found. Found {} out of {}", 
                        services.size(), request.getServiceIds().size());
                }
                room.setServiceList(services);
            } else {
                logger.info("Clearing services for the room");
                room.setServiceList(new ArrayList<>());
            }

            // Update main image if provided
            if (mainImage != null && !mainImage.isEmpty()) {
                String filename = filesStorageService.save(mainImage);
                room.setRoomImg(filename);
            }

            // Handle existing images
            List<Integer> keepImageIds = request.getRoomImageUrls();
            List<RoomImage> currentImages = roomImageService.findByRoomRoomId(room.getRoomId());

            // Remove images that are not in the keep list
            for (RoomImage image : currentImages) {
                if (keepImageIds == null || !keepImageIds.contains(image.getImgId())) {
                    filesStorageService.delete(image.getUrl());
                    roomImageService.deleteById(image.getImgId());
                }
            }
            
            // Add new images
            if (extraImages != null && !extraImages.isEmpty()) {
                for (MultipartFile file : extraImages) {
                    if (!file.isEmpty()) {
                        String filename = filesStorageService.save(file);
                        RoomImage newImage = RoomImage.builder()
                                .room(room)
                                .url(filename)
                                .build();
                        roomImageService.save(newImage);
                    }
                }
            }

            // Save updated room
            Room updatedRoom = roomService.save(room);

            return ResponseEntity.ok(RoomMapper.mapToCreateRoomResponse(updatedRoom, new CreateRoomResponse()));

        } catch (Exception e) {
            logger.error("Error updating room with images", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update room: " + e.getMessage());
        }
    }

    /**
     * Get booking details by ID
     * @param id ID of the booking
     * @return Booking details
     */
    @GetMapping("booking/{id}")
    public ResponseEntity<BookingDetailsDto> getBookingDetails(@PathVariable Integer id) {
        Booking booking = bookingService.findById(id);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(BookingMapper.mapToBookingDetailsDto(booking, new BookingDetailsDto()));
    }

    /**
     * Update booking status
     * @param id ID of the booking
     * @param status New status
     * @return Success indicator
     */
    @PutMapping("booking/status/{id}")
    public ResponseEntity<Boolean> updateBookingStatus(
            @PathVariable Integer id,
            @RequestParam BookingStatus status) {
        Booking updated = bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok(updated != null);
    }

    /**
     * Count rooms for a hotel
     * @param hotelId ID of the hotel
     * @return Number of rooms
     */
    @GetMapping("/rooms/count/{hotelId}")
    public ResponseEntity<Long> countRoomsByHotelId(@PathVariable Integer hotelId) {
        Long count = roomService.countRoomsByHotelId(hotelId);
        return ResponseEntity.ok(count);
    }

    /**
     * Count bookings for a hotel
     * @param hotelId ID of the hotel
     * @return Number of bookings
     */
    @GetMapping("/bookings/count/{hotelId}")
    public ResponseEntity<Long> countBookingsByHotelId(@PathVariable Integer hotelId) {
        Long count = bookingService.countBookingsByHotelId(hotelId);
        return ResponseEntity.ok(count);
    }

    /**
     * Create a room
     * @param roomDto Room data
     * @return Created room
     */
    @PostMapping("/room")
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.save(room));
    }

    /**
     * Get a room by ID
     * @param id ID of the room
     * @return Room details
     */
    @GetMapping("/room/{id}")
    public ResponseEntity<Room> getRoom(@PathVariable Integer id) {
        Room room = roomService.findById(id);
        if (room == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(room);
    }

    /**
     * Delete a room
     * @param id ID of the room to delete
     * @return Success indicator
     */
    @DeleteMapping("/room/{id}")
    public ResponseEntity<Boolean> deleteRoom(@PathVariable Integer id) {
        boolean deleted = roomService.deleteById(id);
        return ResponseEntity.ok(deleted);
    }

    @GetMapping("/reviews/{hotelId}")
    public ResponseEntity<List<ReviewResponseForOwnerDto>> getAllReviews(
            @PathVariable Integer hotelId,
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(required = false) Integer rating) {
        
        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(direction, "createdAt"));

        Page<Review> reviewsPage = reviewService.getReviewsByHotelId(hotelId, query, rating, pageable);
        List<ReviewResponseForOwnerDto> reviewDtos = reviewsPage.getContent().stream()
                .map(review -> mapToReviewResponseForOwner(review))
                .collect(Collectors.toList());

        return ResponseEntity.ok(reviewDtos);
    }

    /**
     * Helper method to map Review to ReviewResponseForOwnerDto
     */
    private ReviewResponseForOwnerDto mapToReviewResponseForOwner(Review review) {
        return ReviewResponseForOwnerDto.builder()
                .reviewId(review.getReviewId())
                .content(review.getContent())
                .rating(review.getRating())
                .guestName(review.getBooking().getUser().getFullName())
                .roomName(review.getBooking().getRoom().getRoomName())
                .createdAt(review.getCreatedAt())
                .ownerReply(review.getOwnerReply())
                .build();
    }

    /**
     * Reply to a review
     * @param reviewId ID of the review
     * @param replyRequest Request containing the reply
     * @return Success status
     */
    @PostMapping("/review/{reviewId}/reply")
    public ResponseEntity<Map<String, Object>> replyToReview(
            @PathVariable Integer reviewId,
            @RequestBody Map<String, String> replyRequest) {
        try {
            String reply = replyRequest.get("reply");
            if (reply == null || reply.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Reply cannot be empty"));
            }

            // Update the review with the reply
            Review review = reviewService.findById(reviewId);
            if (review == null) {
                return ResponseEntity.notFound().build();
            }

            // Assuming you have an ownerReply field in Review model
            review.setOwnerReply(reply.trim());
            reviewService.save(review);

            return ResponseEntity.ok(Map.of("success", true, "message", "Reply added successfully"));
        } catch (Exception e) {
            logger.error("Error replying to review", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Failed to add reply"));
        }
    }
}