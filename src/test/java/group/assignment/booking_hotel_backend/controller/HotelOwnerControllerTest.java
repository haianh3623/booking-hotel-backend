package group.assignment.booking_hotel_backend.controller;

import group.assignment.booking_hotel_backend.models.*;
import group.assignment.booking_hotel_backend.services.*;
import group.assignment.booking_hotel_backend.repository.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelOwnerControllerTest {

    @Mock
    private HotelService hotelService;

    @Mock
    private RoomService roomService;

    @Mock
    private ReviewService reviewService;

    @Mock
    private BookingService bookingService;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private RoomImageService roomImageService;

    @Mock
    private FilesStorageService filesStorageService;

    @InjectMocks
    private HotelOwnerController hotelOwnerController;

    private Booking testBooking;
    private User testUser;
    private Room testRoom;
    private Hotel testHotel;
    private Bill testBill;

    @BeforeEach
    void setUp() {
        // Setup test data
        testUser = User.builder()
                .userId(1)
                .fullName("Test User")
                .email("test@example.com")
                .phone("0123456789")
                .build();

        testHotel = Hotel.builder()
                .hotelId(1)
                .hotelName("Test Hotel")
                .build();

        testRoom = Room.builder()
                .roomId(1)
                .roomName("Test Room")
                .hotel(testHotel)
                .build();

        testBill = Bill.builder()
                .billId(1)
                .totalPrice(1000.0)
                .paidStatus(false)
                .user(testUser)
                .build();

        testBooking = Booking.builder()
                .bookingId(1)
                .checkIn(LocalDateTime.now().plusDays(1))
                .checkOut(LocalDateTime.now().plusDays(2))
                .price(1000.0)
                .status(BookingStatus.PENDING)
                .user(testUser)
                .room(testRoom)
                .bill(testBill)
                .reviewList(new ArrayList<>())
                .build();
    }

    @Test
    void updateBookingStatus_ShouldReturnTrue_WhenUpdateSuccessful() {
        // Arrange
        Integer bookingId = 1;
        BookingStatus status = BookingStatus.CONFIRMED;

        testBooking.setStatus(status);

        when(bookingService.updateBookingStatus(eq(bookingId), eq(status)))
                .thenReturn(testBooking);

        // Act
        ResponseEntity<Boolean> response = hotelOwnerController.updateBookingStatus(bookingId, status);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
    }

    @Test
    void updateBookingStatus_ShouldReturnFalse_WhenUpdateFails() {
        // Arrange
        Integer bookingId = 1;
        BookingStatus status = BookingStatus.CONFIRMED;

        when(bookingService.updateBookingStatus(eq(bookingId), eq(status)))
                .thenReturn(null);

        // Act
        ResponseEntity<Boolean> response = hotelOwnerController.updateBookingStatus(bookingId, status);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody());
    }

    @Test
    void updateBookingStatus_FromPendingToConfirmed_ShouldReturnTrue() {
        // Arrange
        Integer bookingId = 1;
        BookingStatus newStatus = BookingStatus.CONFIRMED;

        // Simulate status change from PENDING to CONFIRMED
        testBooking.setStatus(BookingStatus.PENDING);
        Booking updatedBooking = Booking.builder()
                .bookingId(1)
                .checkIn(testBooking.getCheckIn())
                .checkOut(testBooking.getCheckOut())
                .price(testBooking.getPrice())
                .status(newStatus)
                .user(testUser)
                .room(testRoom)
                .bill(testBill)
                .reviewList(new ArrayList<>())
                .build();

        when(bookingService.updateBookingStatus(eq(bookingId), eq(newStatus)))
                .thenReturn(updatedBooking);

        // Act
        ResponseEntity<Boolean> response = hotelOwnerController.updateBookingStatus(bookingId, newStatus);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
    }

    @Test
    void updateBookingStatus_FromPendingToCancelled_ShouldReturnTrue() {
        // Arrange
        Integer bookingId = 1;
        BookingStatus newStatus = BookingStatus.CANCELLED;

        // Simulate status change from PENDING to CANCELLED
        testBooking.setStatus(BookingStatus.PENDING);
        Booking cancelledBooking = Booking.builder()
                .bookingId(1)
                .checkIn(testBooking.getCheckIn())
                .checkOut(testBooking.getCheckOut())
                .price(testBooking.getPrice())
                .status(newStatus)
                .user(testUser)
                .room(testRoom)
                .bill(testBill)
                .reviewList(new ArrayList<>())
                .build();

        when(bookingService.updateBookingStatus(eq(bookingId), eq(newStatus)))
                .thenReturn(cancelledBooking);

        // Act
        ResponseEntity<Boolean> response = hotelOwnerController.updateBookingStatus(bookingId, newStatus);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
    }

    @Test
    void updateBookingStatus_WithNonExistentBooking_ShouldReturnFalse() {
        // Arrange
        Integer nonExistentBookingId = 999;
        BookingStatus status = BookingStatus.CONFIRMED;

        when(bookingService.updateBookingStatus(eq(nonExistentBookingId), eq(status)))
                .thenReturn(null);

        // Act
        ResponseEntity<Boolean> response = hotelOwnerController.updateBookingStatus(nonExistentBookingId, status);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody());
    }

    @Test
    void updateBookingStatus_WithPendingStatus_ShouldReturnTrue() {
        // Arrange
        Integer bookingId = 1;
        BookingStatus status = BookingStatus.PENDING;

        testBooking.setStatus(status);

        when(bookingService.updateBookingStatus(eq(bookingId), eq(status)))
                .thenReturn(testBooking);

        // Act
        ResponseEntity<Boolean> response = hotelOwnerController.updateBookingStatus(bookingId, status);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
    }
}