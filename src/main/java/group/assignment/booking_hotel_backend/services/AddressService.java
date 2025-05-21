package group.assignment.booking_hotel_backend.services;

import group.assignment.booking_hotel_backend.dto.AddressDto;
import group.assignment.booking_hotel_backend.models.Address;

import java.io.StringBufferInputStream;
import java.util.List;

public interface AddressService {
    List<String> findDistrictByCity(String city);
    List<String> findAllCity();
}
