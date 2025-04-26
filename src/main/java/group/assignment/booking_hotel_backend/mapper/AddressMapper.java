package group.assignment.booking_hotel_backend.mapper;

import group.assignment.booking_hotel_backend.dto.AddressDto;
import group.assignment.booking_hotel_backend.dto.HotelDto;
import group.assignment.booking_hotel_backend.models.Address;
import group.assignment.booking_hotel_backend.models.Hotel;

public class AddressMapper {
    public static AddressDto mapToAddressDto(Address address, AddressDto addressDto) {
        addressDto.setCity(address.getCity());
        addressDto.setWard(address.getWard());
        addressDto.setDistrict(address.getDistrict());
        addressDto.setSpecificAddress(address.getSpecificAddress());
        return addressDto;
    }

    public static Address mapToAddress(AddressDto addressDto, Address address) {
        address.setCity(addressDto.getCity());
        address.setWard(addressDto.getWard());
        address.setDistrict(addressDto.getDistrict());
        address.setSpecificAddress(addressDto.getSpecificAddress());
        return address;
    }
}
