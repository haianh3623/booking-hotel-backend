package group.assignment.booking_hotel_backend.services;

import group.assignment.booking_hotel_backend.dto.BookingRequestDto;
import group.assignment.booking_hotel_backend.dto.BookingResponseDto;
import group.assignment.booking_hotel_backend.dto.BookingSearchRequest;
import group.assignment.booking_hotel_backend.dto.BookingSearchResponse;
import group.assignment.booking_hotel_backend.dto.BookingStatsDto;
import group.assignment.booking_hotel_backend.models.Booking;
import group.assignment.booking_hotel_backend.models.BookingStatus;
import group.assignment.booking_hotel_backend.models.Room;

import java.util.List;

public interface BookingService {
    public List<BookingSearchResponse> searchAvailableRooms(BookingSearchRequest request);
    BookingResponseDto createBooking(BookingRequestDto request);
    List<Booking> findAll();
//    BookingResponseDto findById(Integer id);
    Booking findById(Integer id);
    Booking updateBooking(Integer id, BookingRequestDto request);
    Booking update(Booking booking);
    void deleteById(Integer id);
    Booking updateBookingStatus(Integer bookingId, BookingStatus newStatus);
    List<Booking> findByUserId(Integer userId);
    List<BookingStatsDto> getBookingStatsLastNDaysForHotel(int hotelId, int days);
    List<Booking> getCurrentBookingForHotel(int hotelId);
}
