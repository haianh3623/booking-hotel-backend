package group.assignment.booking_hotel_backend.mapper;

import group.assignment.booking_hotel_backend.dto.BillResponseDto;
import group.assignment.booking_hotel_backend.dto.BookingServiceResponse;
import group.assignment.booking_hotel_backend.dto.ServiceDto;
import group.assignment.booking_hotel_backend.models.Bill;
import group.assignment.booking_hotel_backend.models.Service;

public class BillMapper {
    public static BillResponseDto mapToBillResponseDto(Bill bill, BillResponseDto response) {
        response.setBillId(bill.getBillId());
        response.setTotalPrice(bill.getTotalPrice());
        response.setPaidStatus(bill.getPaidStatus());
        response.setUserId(bill.getUser().getUserId());
        return response;
    }
}
