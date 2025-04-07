package group.assignment.booking_hotel_backend.mapper;

import group.assignment.booking_hotel_backend.dto.AddressDto;
import group.assignment.booking_hotel_backend.dto.HotelDto;
import group.assignment.booking_hotel_backend.models.Address;
import group.assignment.booking_hotel_backend.models.Hotel;
import group.assignment.booking_hotel_backend.models.User;

public class HotelMapper {
    public static HotelDto mapToHotelDto(Hotel hotel, HotelDto hotelDto) {
        hotelDto.setHotelId(hotel.getHotelId());
        hotelDto.setHotelName(hotel.getHotelName());
        hotelDto.setUserId(hotel.getUser().getUserId());

        AddressDto addressDto = new AddressDto();
        addressDto.setCity(hotel.getAddress().getCity());
        addressDto.setDistrict(hotel.getAddress().getDistrict());
        addressDto.setWard(hotel.getAddress().getWard());
        addressDto.setSpecificAddress(hotel.getAddress().getSpecificAddress());

        hotelDto.setAddress(addressDto);
        return hotelDto;
    }

//    public static Hotel mapToHotel(HotelDto dto, User user) {
//        Hotel hotel = new Hotel();
//        hotel.setHotelId(dto.getHotelId());
//        hotel.setHotelName(dto.getHotelName());
//        hotel.setUser(user);
//
//        Address address = new Address();
//        address.setCity(dto.getAddress().getCity());
//        address.setDistrict(dto.getAddress().getDistrict());
//        address.setWard(dto.getAddress().getWard());
//        address.setSpecificAddress(dto.getAddress().getSpecificAddress());
//
//        hotel.setAddress(address);
//
//        return hotel;
//    }

}
