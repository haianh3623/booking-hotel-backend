package group.assignment.booking_hotel_backend.mapper;

import group.assignment.booking_hotel_backend.dto.BookingServiceResponse;
import group.assignment.booking_hotel_backend.dto.RegistrationRequest;
import group.assignment.booking_hotel_backend.models.Service;
import group.assignment.booking_hotel_backend.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ServiceMapper {
    public static BookingServiceResponse mapToBookingServiceResponse(Service service, BookingServiceResponse response) {
        response.setServiceId(service.getServiceId());
        response.setServiceName(service.getServiceName());
        return response;
    }
}
