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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

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
        testUser = User.builder()
                .userId(1)
                .fullName("Test User")
                .email("test@example.com")
                .phone("0123456789")
                .build();

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
        verify(firebaseMessagingService, never()).sendBookingStatusUpdateNotification(any(Booking.class));
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
        verify(firebaseMessagingService, never()).sendBookingStatusUpdateNotification(any(Booking.class));
    }

    @Test
    void updateBookingStatus_ShouldHandleFirebaseFailure_Gracefully() {
        // Arrange
        Integer bookingId = 1;
        BookingStatus newStatus = BookingStatus.CONFIRMED;
        
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);
        doThrow(new RuntimeException("Firebase error")).when(firebaseMessagingService)
                .sendBookingStatusUpdateNotification(any(Booking.class));

        // Act
        Booking result = bookingService.updateBookingStatus(bookingId, newStatus);

        // Assert
        assertNotNull(result);
        assertEquals(newStatus, result.getStatus());
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
}
