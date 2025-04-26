package group.assignment.booking_hotel_backend.mapper;

import group.assignment.booking_hotel_backend.dto.*;
import group.assignment.booking_hotel_backend.models.*;

import java.util.ArrayList;
import java.util.List;

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
        response.setCreatedAt(booking.getCreatedAt());
        List<Integer> reviewIdList = new ArrayList<>();
        List<Review> reviewList = booking.getReviewList();
        if (reviewList != null) {
            for (Review review : reviewList){
                reviewIdList.add(review.getReviewId());
            }
        }
        response.setReviewIdList(reviewIdList);
        return response;
    }

    public static BookingScheduleResponse mapToBookingScheduleResponse(Booking booking, BookingScheduleResponse response, UserDto user, RoomResponseDto room) {
        response.setBookingId(booking.getBookingId());
        response.setCheckIn(booking.getCheckIn());
        response.setCheckOut(booking.getCheckOut());
        response.setPrice(booking.getPrice());
        response.setStatus(booking.getStatus().name());
        response.setRoomDto(room);
        response.setUserDto(user);
        response.setCreatedAt(booking.getCreatedAt());
        return response;
    }

    public static BookingDto mapToBookingDto(Booking booking, BookingDto dto) {
        dto.setBookingId(booking.getBookingId());
        dto.setCheckIn(booking.getCheckIn());
        dto.setCheckOut(booking.getCheckOut());
        dto.setPrice(booking.getPrice());
        dto.setStatus(booking.getStatus());
        dto.setUserId(booking.getUser().getUserId());
        dto.setRoomId(booking.getRoom().getRoomId());
        dto.setBillId(booking.getBill() != null ? booking.getBill().getBillId() : null);
        dto.setCreatedAt(booking.getCreatedAt());
        return dto;
    }

}
