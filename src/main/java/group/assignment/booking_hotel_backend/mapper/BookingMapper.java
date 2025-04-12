package group.assignment.booking_hotel_backend.mapper;

import group.assignment.booking_hotel_backend.dto.*;
import group.assignment.booking_hotel_backend.models.*;

public class BookingMapper {
    public static BookingResponseDto mapToBookingResponseDto(Booking booking, BookingResponseDto response) {
        response.setBookingId(booking.getBookingId());
        response.setCheckIn(booking.getCheckIn());
        response.setCheckOut(booking.getCheckOut());
        response.setPrice(booking.getPrice());
        response.setStatus(booking.getStatus().name());
        response.setUserId(booking.getUser().getUserId());
        response.setRoomId(booking.getRoom().getRoomId());
        response.setBillId(booking.getBill() != null ? booking.getBill().getBillId() : null);
        return response;
    }
}
