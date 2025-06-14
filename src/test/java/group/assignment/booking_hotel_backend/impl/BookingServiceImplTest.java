package group.assignment.booking_hotel_backend.impl;

import group.assignment.booking_hotel_backend.dto.BookingRequestDto;
import group.assignment.booking_hotel_backend.dto.BookingResponseDto;

import group.assignment.booking_hotel_backend.dto.BookingSearchRequest;
import group.assignment.booking_hotel_backend.dto.BookingSearchResponse;
import group.assignment.booking_hotel_backend.models.*;
import group.assignment.booking_hotel_backend.repository.*;
import group.assignment.booking_hotel_backend.services.FirebaseMessagingService;
import group.assignment.booking_hotel_backend.services.NotificationService;
import group.assignment.booking_hotel_backend.services.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BillRepository billRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private FirebaseMessagingService firebaseMessagingService;

    @Mock
    private HotelRepository hotelRepository;

    private BookingRequestDto request;

    private User mockUser;
    private Room mockRoom;

    @BeforeEach
    void setUp() {
        // Mock request
        request = BookingRequestDto.builder()
                .userId(1)
                .roomId(10)
                .price(500000.0D)
                .checkIn(LocalDateTime.of(2025, 6, 15, 14, 0))
                .checkOut(LocalDateTime.of(2025, 6, 16, 12, 0))
                .build();

        mockUser = new User();
        mockUser.setUserId(1);

        mockRoom = new Room();
        mockRoom.setRoomId(10);
    }

    // Tao booking
    @Test
    void createBooking_shouldCreateBillAndBookingAndReturnDto() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
        when(roomRepository.findById(10)).thenReturn(Optional.of(mockRoom));
        when(billRepository.save(any(Bill.class))).thenAnswer(inv -> {
            Bill bill = inv.getArgument(0);
            bill.setBillId(100); // Mock generated ID
            return bill;
        });
        when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> {
            Booking booking = inv.getArgument(0);
            booking.setBookingId(200); // Mock generated ID
            return booking;
        });

        // Act
        BookingResponseDto result = bookingService.createBooking(request);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getBookingId());
        assertEquals("PENDING", result.getStatus());
        assertEquals(Double.valueOf(500000.0), result.getPrice());
        assertEquals(1, result.getUserId());

        verify(userRepository).findById(1);
        verify(roomRepository).findById(10);
        verify(billRepository).save(any(Bill.class));
        verify(bookingRepository).save(any(Booking.class));
    }



    // Lấy đặt phòng chi tiết, đặt phòng đã tồn tại
    @Test
    void findById_shouldReturnBooking_whenExists() {
        // Arrange
        int bookingId = 1;
        Booking booking = Booking.builder()
                .bookingId(bookingId)
                .status(BookingStatus.PENDING)
                .build();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        // Act
        Booking result = bookingService.findById(bookingId);

        // Assert
        assertNotNull(result);
        assertEquals(bookingId, result.getBookingId());
        verify(bookingRepository).findById(bookingId);
    }

    // Lấy đặt phòng chi tiết, đặt phòng chưa tồn tại
    @Test
    void findById_shouldThrowException_whenNotFound() {
        // Arrange
        int bookingId = 999;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookingService.findById(bookingId);
        });

        assertEquals("Booking not found", exception.getMessage());
        verify(bookingRepository).findById(bookingId);
    }


    // Cập nhật trang thái đặt phòng
    @Test
    void updateBookingStatus_shouldUpdateStatusAndSendNotifications() {
        // Arrange
        Booking booking = Booking.builder()
                .bookingId(100)
                .status(BookingStatus.PENDING) // old status
                .build();

        when(bookingRepository.findById(100)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));

        // Inject mock firebase service
        bookingService.setFirebaseMessagingService(firebaseMessagingService);

        // Act
        Booking result = bookingService.updateBookingStatus(100, BookingStatus.CONFIRMED);

        // Assert
        assertEquals(BookingStatus.CONFIRMED, result.getStatus());
        verify(firebaseMessagingService).sendBookingStatusUpdateNotification(any(Booking.class));
    }



    @Test
    void updateBookingStatus_shouldHandleCancelledStatus() {
        // Arrange
        Booking booking = Booking.builder()
                .bookingId(101)
                .status(BookingStatus.PENDING)
                .user(new User())
                .room(new Room())
                .build();

        Booking updatedBooking = Booking.builder()
                .bookingId(101)
                .status(BookingStatus.CANCELLED)
                .user(new User())
                .room(new Room())
                .build();

        when(bookingRepository.findById(101)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(updatedBooking);

        // Act
        Booking result = bookingService.updateBookingStatus(101, BookingStatus.CANCELLED);

        // Assert
        assertEquals(BookingStatus.CANCELLED, result.getStatus());
        verify(notificationService).handleBookingEvent(updatedBooking, "BOOKING_CANCEL");
    }

    @Test
    void updateBookingStatus_shouldThrowExceptionWhenBookingNotFound() {
        // Arrange
        when(bookingRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookingService.updateBookingStatus(999, BookingStatus.CONFIRMED));
        assertEquals("Booking not found", exception.getMessage());
    }

    // Tìm phòng trống, tất cả các trường đều khớp
    @Test
    void searchAvailableRooms_ShouldReturnMatchingRoom_WhenAllConditionsMet() {
        // Arrange: Mock Room
        Room room = Room.builder()
                .roomId(1)
                .roomName("Deluxe Room")
                .roomImg("deluxe.jpg")
                .description("A nice deluxe room")
                .maxOccupancy(4)
                .bedNumber(2)
                .pricePerNight(100.0)
                .numChildrenFree(0)
                .standardOccupancy(2)
                .extraAdult(10.0)
                .comboPrice2h(50.0)
                .extraHourPrice(30.0)
                .area(50.0)
                .serviceList(List.of(
                        Service.builder().serviceName("WiFi").build(),
                        Service.builder().serviceName("Breakfast").build()
                ))
                .bookingList(new ArrayList<>()) // No bookings, room is available
                .build();

        // Arrange: Mock Hotel with Address
        Address address = Address.builder()
                .city("Hanoi")
                .district("Hoan Kiem")
                .ward("Trang Tien")
                .specificAddress("123 Le Loi")
                .build();

        Hotel hotel = Hotel.builder()
                .hotelName("Luxury Hotel")
                .address(address)
                .roomList(List.of(room))
                .build();

        // Arrange: Booking Search Request
        BookingSearchRequest request = BookingSearchRequest.builder()
                .infoSearch("luxury") // keyword matches hotel name
                .city("Hanoi")
                .district("Hoan Kiem")
                .checkInDate("2025-06-20")
                .checkOutDate("2025-06-22")
                .checkInTime("14:00")
                .checkOutTime("12:00")
                .adults(2)
                .children(1)
                .bedNumber(2)
                .priceFrom(50.0)
                .priceTo(30000.0)
                .services(List.of("WiFi", "Breakfast"))
                .sortBy("price_asc")
                .build();

        // Mock: Hotel Repository
        when(hotelRepository.findByAddressCityAndAddressDistrict("Hanoi", "Hoan Kiem"))
                .thenReturn(List.of(hotel));

        // Act: Call service method
        List<BookingSearchResponse> results = bookingService.searchAvailableRooms(request);

        // Assert
        assertEquals(1, results.size(), "Should return one matching room");

        BookingSearchResponse res = results.get(0);
        assertEquals("Deluxe Room", res.getRoomName());
        assertEquals("Luxury Hotel", res.getHotelName());
        assertEquals("123 Le Loi", res.getAddress());
        assertEquals(2, res.getAdults());
        assertEquals(1, res.getChildren());
        assertEquals(2, res.getBedNumber());
        assertEquals(List.of("WiFi", "Breakfast"), res.getServices());
        assertTrue(res.getPrice() >= 50.0 && res.getPrice() <= 30000.0, "Price should be within requested range");
    }


    //  Tìm phòng trống khi phòng đã có người đặt trong thời gian mà người dùng yêu cầu
    @Test
    void searchAvailableRooms_ShouldReturnEmpty_WhenRoomIsNotAvailable() {
        // Arrange: Booking đã có trong khoảng thời gian tìm kiếm
        Booking booking = Booking.builder()
                .checkIn(LocalDateTime.of(2025, 6, 20, 13, 0))
                .checkOut(LocalDateTime.of(2025, 6, 22, 13, 0))
                .reviewList(List.of(Review.builder().rating(4).build()))
                .build();

        // Arrange: Room đã có booking trùng thời gian
        Room room = Room.builder()
                .roomId(2)
                .roomName("Standard Room")
                .roomImg("standard.jpg")
                .description("A standard room for test")
                .maxOccupancy(4)
                .bedNumber(1)
                .pricePerNight(80.0)
                .numChildrenFree(0)
                .standardOccupancy(2)
                .extraAdult(10.0)
                .comboPrice2h(50.0)
                .extraHourPrice(30.0)
                .area(30.0)
                .serviceList(List.of(Service.builder().serviceName("WiFi").build()))
                .bookingList(List.of(booking)) // Có người đặt rồi
                .build();

        // Arrange: Hotel chứa phòng
        Hotel hotel = Hotel.builder()
                .hotelName("Budget Hotel")
                .address(Address.builder()
                        .city("Hanoi")
                        .district("Ba Dinh")
                        .ward("Kim Ma")
                        .specificAddress("No 9")
                        .build())
                .roomList(List.of(room))
                .build();

        // Arrange: Request tìm phòng đúng thời gian đã có người đặt
        BookingSearchRequest request = BookingSearchRequest.builder()
                .infoSearch("budget")
                .city("Hanoi")
                .district("Ba Dinh")
                .checkInDate("2025-06-20")
                .checkOutDate("2025-06-22")
                .checkInTime("14:00")
                .checkOutTime("12:00")
                .adults(1)
                .children(0)
                .bedNumber(1)
                .services(List.of("WiFi"))
                .sortBy("price_asc")
                .priceFrom(50.0)
                .priceTo(200000.0)
                .build();

        // Mock repository
        when(hotelRepository.findByAddressCityAndAddressDistrict("Hanoi", "Ba Dinh"))
                .thenReturn(List.of(hotel));

        // Act
        List<BookingSearchResponse> results = bookingService.searchAvailableRooms(request);

        // Assert
        assertTrue(results.isEmpty(), "No available room should be returned due to overlapping booking");
    }
}

