package group.assignment.booking_hotel_backend.services.impl;

import group.assignment.booking_hotel_backend.models.*;
import group.assignment.booking_hotel_backend.repository.*;
import group.assignment.booking_hotel_backend.services.FirebaseMessagingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BillRepository billRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FirebaseMessagingService firebaseMessagingService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Booking testBooking;
    private User testUser;
    private Room testRoom;
    private Hotel testHotel;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = new User();
        testUser.setUserId(1);
        testUser.setFullName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPhone("0123456789");

        // Setup test hotel
        testHotel = Hotel.builder()
                .hotelId(1)
                .hotelName("Test Hotel")
                .build();

        // Setup test room
        testRoom = Room.builder()
                .roomId(1)
                .roomName("Test Room")
                .hotel(testHotel)
                .build();

        // Setup test booking
        testBooking = Booking.builder()
                .bookingId(1)
                .checkIn(LocalDateTime.now().plusDays(1))
                .checkOut(LocalDateTime.now().plusDays(2))
                .price(1000.0)
                .status(BookingStatus.PENDING)
                .user(testUser)
                .room(testRoom)
                .build();


        ReflectionTestUtils.setField(bookingService, "firebaseMessagingService", firebaseMessagingService);
    }

    @Test
    void updateBookingStatus_ShouldUpdateStatusSuccessfully_WhenBookingExists() {
        // Arrange
        Integer bookingId = 1;
        BookingStatus newStatus = BookingStatus.CONFIRMED;
        
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        // Act
        Booking result = bookingService.updateBookingStatus(bookingId, newStatus);

        // Assert
        assertNotNull(result);
        assertEquals(newStatus, result.getStatus());
        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository).save(testBooking);
        verify(firebaseMessagingService).sendBookingStatusUpdateNotification(testBooking);
    }

    @Test
    void updateBookingStatus_ShouldThrowException_WhenBookingNotFound() {
        // Arrange
        Integer bookingId = 999;
        BookingStatus newStatus = BookingStatus.CONFIRMED;
        
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> bookingService.updateBookingStatus(bookingId, newStatus));
        
        assertEquals("Booking not found", exception.getMessage());
        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository, never()).save(any(Booking.class));
        verifyNoInteractions(firebaseMessagingService);
    }

    @Test
    void updateBookingStatus_ShouldSendNotification_WhenStatusChanges() {
        // Arrange
        Integer bookingId = 1;
        BookingStatus oldStatus = BookingStatus.PENDING;
        BookingStatus newStatus = BookingStatus.CONFIRMED;
        
        testBooking.setStatus(oldStatus);
        
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        // Act
        bookingService.updateBookingStatus(bookingId, newStatus);

        // Assert
        verify(firebaseMessagingService).sendBookingStatusUpdateNotification(testBooking);
    }

    @Test
    void updateBookingStatus_ShouldNotSendNotification_WhenStatusUnchanged() {
        // Arrange
        Integer bookingId = 1;
        BookingStatus currentStatus = BookingStatus.CONFIRMED;
        
        testBooking.setStatus(currentStatus);
        
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        // Act
        bookingService.updateBookingStatus(bookingId, currentStatus);

        // Assert
        verifyNoInteractions(firebaseMessagingService);
    }

    @Test
    void updateBookingStatus_ShouldHandleFirebaseFailure_Gracefully() {
        // Arrange
        Integer bookingId = 1;
        BookingStatus newStatus = BookingStatus.CONFIRMED;
        
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        // Act & Assert - Should not throw exception even if Firebase fails
        assertDoesNotThrow(() -> {
            Booking result = bookingService.updateBookingStatus(bookingId, newStatus);
            assertNotNull(result);
            assertEquals(newStatus, result.getStatus());
        });
        
        verify(bookingRepository).save(testBooking);
    }

    @Test
    void updateBookingStatus_ToCancelled_ShouldUpdateCorrectly() {
        // Arrange
        Integer bookingId = 1;
        BookingStatus newStatus = BookingStatus.CANCELLED;
        
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        // Act
        Booking result = bookingService.updateBookingStatus(bookingId, newStatus);

        // Assert
        assertNotNull(result);
        assertEquals(BookingStatus.CANCELLED, result.getStatus());
        verify(firebaseMessagingService).sendBookingStatusUpdateNotification(testBooking);
    }

    @Test
    void updateBookingStatus_ShouldNotSendNotification_WhenFirebaseServiceIsNull() {
        // Arrange
        Integer bookingId = 1;
        BookingStatus newStatus = BookingStatus.CONFIRMED;
        
        // Set FirebaseMessagingService to null to simulate when it's not available
        ReflectionTestUtils.setField(bookingService, "firebaseMessagingService", null);
        
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> {
            Booking result = bookingService.updateBookingStatus(bookingId, newStatus);
            assertNotNull(result);
            assertEquals(newStatus, result.getStatus());
        });
        
        verify(bookingRepository).save(testBooking);
        // Firebase service should not be called since it's null
        verifyNoInteractions(firebaseMessagingService);
    }

    @Test
    void findById_ShouldReturnBooking_WhenBookingExists() {
        // Arrange
        Integer bookingId = 1;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(testBooking));

        // Act
        Booking result = bookingService.findById(bookingId);

        // Assert
        assertNotNull(result);
        assertEquals(testBooking.getBookingId(), result.getBookingId());
        verify(bookingRepository).findById(bookingId);
    }

    @Test
    void findById_ShouldThrowException_WhenBookingNotFound() {
        // Arrange
        Integer bookingId = 999;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> bookingService.findById(bookingId));
        
        assertEquals("Booking not found", exception.getMessage());
        verify(bookingRepository).findById(bookingId);
    }
}