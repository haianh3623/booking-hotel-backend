package group.assignment.booking_hotel_backend.mapper;

import group.assignment.booking_hotel_backend.dto.*;
import group.assignment.booking_hotel_backend.models.Hotel;
import group.assignment.booking_hotel_backend.models.Room;
import group.assignment.booking_hotel_backend.models.RoomImage;
import group.assignment.booking_hotel_backend.models.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class RoomMapper {
    public static Room mapToRoom(CreateRoomRequest request, Hotel hotel, MultipartFile mainImage, List<Service> services) {
        Room room = new Room();
        room.setRoomName(request.getRoomName());
        room.setArea(request.getArea());
        room.setComboPrice2h(request.getComboPrice2h());
        room.setPricePerNight(request.getPricePerNight());
        room.setExtraHourPrice(request.getExtraHourPrice());
        room.setStandardOccupancy(request.getStandardOccupancy());
        room.setMaxOccupancy(request.getMaxOccupancy());
        room.setNumChildrenFree(request.getNumChildrenFree());
        room.setBedNumber(request.getBedNumber());
        room.setExtraAdult(request.getExtraAdult());
        room.setDescription(request.getDescription());
        room.setHotel(hotel);

        if (mainImage != null && !mainImage.isEmpty()) {
            room.setRoomImg(mainImage.getOriginalFilename());
        }

        if (services != null && !services.isEmpty()) {
            room.setServiceList(services);
        }

        return room;
    }

    public static CreateRoomResponse mapToCreateRoomResponse(Room room, CreateRoomResponse response) {
        response.setRoomName(room.getRoomName());
        response.setArea(room.getArea());
        response.setComboPrice2h(room.getComboPrice2h());
        response.setPricePerNight(room.getPricePerNight());
        response.setExtraHourPrice(room.getExtraHourPrice());
        response.setStandardOccupancy(room.getStandardOccupancy());
        response.setMaxOccupancy(room.getMaxOccupancy());
        response.setNumChildrenFree(room.getNumChildrenFree());
        response.setRoomImg(room.getRoomImg());
        response.setBedNumber(room.getBedNumber());
        response.setExtraAdult(room.getExtraAdult());
        response.setDescription(room.getDescription());
        response.setHotelDto(HotelMapper.mapToHotelDto(room.getHotel(), new HotelDto()));
        if (room.getServiceList() != null) {
            List<ServiceDto> serviceDtos = new ArrayList<>();
            for (Service service : room.getServiceList()){
                ServiceDto serviceDto = ServiceMapper.mapToServiceDto(service, new ServiceDto());
                serviceDtos.add(serviceDto);
            }
            response.setServiceDtoList(serviceDtos);
        }

        List<RoomImageDto> imageUrls = new ArrayList<>();
        if (room.getRoomImageList() != null) {
            List<RoomImage> roomImageList = room.getRoomImageList();
            for (RoomImage roomImage : roomImageList) {
                RoomImageDto roomImageDto = RoomImageMapper.mapToRoomImageDto(roomImage, new RoomImageDto());
                imageUrls.add(roomImageDto);
            }
            response.setRoomImageUrls(imageUrls);
        }
        return response;
    }


    public static RoomResponseDto mapToRoomDto(Room room, RoomResponseDto roomDto) {
        roomDto.setRoomName(room.getRoomName());
        roomDto.setArea(room.getArea());
        roomDto.setComboPrice2h(room.getComboPrice2h());
        roomDto.setPricePerNight(room.getPricePerNight());
        roomDto.setExtraHourPrice(room.getExtraHourPrice());
        roomDto.setStandardOccupancy(room.getStandardOccupancy());
        roomDto.setMaxOccupancy(room.getMaxOccupancy());
        roomDto.setNumChildrenFree(room.getNumChildrenFree());
        roomDto.setRoomImg(room.getRoomImg());
        roomDto.setBedNumber(room.getBedNumber());
        roomDto.setExtraAdult(room.getExtraAdult());
        roomDto.setDescription(room.getDescription());
        roomDto.setHotelDto(HotelMapper.mapToHotelDto(room.getHotel(), new HotelDto()));
        if (room.getServiceList() != null) {
            List<ServiceDto> serviceDtos = new ArrayList<>();
            for (Service service : room.getServiceList()){
                ServiceDto serviceDto = ServiceMapper.mapToServiceDto(service, new ServiceDto());
                serviceDtos.add(serviceDto);
            }
            roomDto.setServiceDtoList(serviceDtos);
        }

        List<RoomImageDto> imageUrls = new ArrayList<>();
        if (room.getRoomImageList() != null) {
            List<RoomImage> roomImageList = room.getRoomImageList();
            for (RoomImage roomImage : roomImageList) {
                RoomImageDto roomImageDto = RoomImageMapper.mapToRoomImageDto(roomImage, new RoomImageDto());
                imageUrls.add(roomImageDto);
            }
            roomDto.setRoomImageUrls(imageUrls);
        }
        return roomDto;
    }
}
