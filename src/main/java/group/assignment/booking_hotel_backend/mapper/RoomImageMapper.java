package group.assignment.booking_hotel_backend.mapper;

import group.assignment.booking_hotel_backend.dto.AddressDto;
import group.assignment.booking_hotel_backend.dto.HotelDto;
import group.assignment.booking_hotel_backend.dto.RoomImageDto;
import group.assignment.booking_hotel_backend.models.Hotel;
import group.assignment.booking_hotel_backend.models.RoomImage;

public class RoomImageMapper {
    public static RoomImageDto mapToRoomImageDto(RoomImage roomImage, RoomImageDto roomImageDto) {
        roomImageDto.setImgId(roomImage.getImgId());
        roomImageDto.setUrl(roomImage.getUrl());
        return roomImageDto;
    }
}
