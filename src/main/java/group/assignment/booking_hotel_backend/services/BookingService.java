package group.assignment.booking_hotel_backend.services;

import group.assignment.booking_hotel_backend.dto.BookingRequestDto;
import group.assignment.booking_hotel_backend.dto.BookingResponseDto;
import group.assignment.booking_hotel_backend.dto.BookingSearchRequest;
import group.assignment.booking_hotel_backend.dto.BookingSearchResponse;
import group.assignment.booking_hotel_backend.dto.BookingStatsDto;
import group.assignment.booking_hotel_backend.dto.BookingStatsDto;
import group.assignment.booking_hotel_backend.dto.*;
import group.assignment.booking_hotel_backend.models.Booking;
import group.assignment.booking_hotel_backend.models.BookingStatus;
import group.assignment.booking_hotel_backend.models.Room;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    Page<Booking> getBookingsByHotelId(Integer hotelId, String query, Pageable pageable);
    Long countBookingsByHotelId(Integer hotelId);
    Long count();
    Map<String, Double> getDailyRevenueThisMonth();
    Double getTotalRevenueBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
    AdminRevenueResponse getRevenueAndBookingDetails(LocalDateTime startDate, LocalDateTime endDate);
    List<Booking> findAllBookingsByHotelOwner(Integer userId);
}
