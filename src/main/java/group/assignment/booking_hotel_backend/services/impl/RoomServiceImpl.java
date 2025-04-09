package group.assignment.booking_hotel_backend.services.impl;

import group.assignment.booking_hotel_backend.dto.RoomDto;
import group.assignment.booking_hotel_backend.models.Hotel;
import group.assignment.booking_hotel_backend.models.Room;
import group.assignment.booking_hotel_backend.models.Service;
import group.assignment.booking_hotel_backend.repository.HotelRepository;
import group.assignment.booking_hotel_backend.repository.RoomRepository;
import group.assignment.booking_hotel_backend.repository.ServiceRepository;
import group.assignment.booking_hotel_backend.services.FilesStorageService;
import group.assignment.booking_hotel_backend.services.RoomService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final ServiceRepository serviceRepository;
    private final FilesStorageService filesStorageService;

    @Override
    public Room save(RoomDto roomDto) {
        Room room = new Room();
        room.setRoomName(roomDto.getRoomName());
        room.setArea(roomDto.getArea());
        room.setComboPrice2h(roomDto.getComboPrice2h());
        room.setPricePerNight(roomDto.getPricePerNight());
        room.setExtraHourPrice(roomDto.getExtraHourPrice());
        room.setStandardOccupancy(roomDto.getStandardOccupancy());
        room.setMaxOccupancy(roomDto.getMaxOccupancy());
        room.setNumChildrenFree(roomDto.getNumChildrenFree());
        room.setBedNumber(roomDto.getBedNumber());
        room.setExtraAdult(roomDto.getExtraAdult());
        room.setDescription(roomDto.getDescription());
        if (roomDto.getRoomImg() != null && !roomDto.getRoomImg().isEmpty()) {
            filesStorageService.save(roomDto.getRoomImg());
            room.setRoomImg(roomDto.getRoomImg().getOriginalFilename());
        }
        Hotel hotel = hotelRepository.findById(roomDto.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        room.setHotel(hotel);
        if (roomDto.getServiceIds() != null) {
            List<Service> services = serviceRepository.findAllById(roomDto.getServiceIds());
            room.setServiceList(services);
        }
        return roomRepository.save(room);
    }

    @Override
    public Room save(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public Room update(Integer id, RoomDto roomDto) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room not found"));
        room.setRoomName(roomDto.getRoomName());
        room.setArea(roomDto.getArea());
        room.setComboPrice2h(roomDto.getComboPrice2h());
        room.setPricePerNight(roomDto.getPricePerNight());
        room.setExtraHourPrice(roomDto.getExtraHourPrice());
        room.setStandardOccupancy(roomDto.getStandardOccupancy());
        room.setMaxOccupancy(roomDto.getMaxOccupancy());
        room.setNumChildrenFree(roomDto.getNumChildrenFree());
        room.setBedNumber(roomDto.getBedNumber());
        room.setExtraAdult(roomDto.getExtraAdult());
        room.setDescription(roomDto.getDescription());

        // Update image nếu có file mới
        if (roomDto.getRoomImg() != null && !roomDto.getRoomImg().isEmpty()) {
            filesStorageService.save(roomDto.getRoomImg());
            room.setRoomImg(roomDto.getRoomImg().getOriginalFilename());
        }

        Hotel hotel = hotelRepository.findById(roomDto.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        room.setHotel(hotel);

        List<Service> services = serviceRepository.findAllById(roomDto.getServiceIds());
        room.setServiceList(services);

        return roomRepository.save(room);
    }

    @Override
    public boolean deleteById(Integer id) {
        Optional<Room> roomOpt = roomRepository.findById(id);
        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            if (room.getRoomImg() != null) {
                filesStorageService.delete(room.getRoomImg());
            }
            roomRepository.delete(room);
            return true;
        }
        return false;
    }

    @Override
    public Room findById(Integer id) {
        return roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room not found"));
    }

    @Override
    public List<Room> findAll() {
        return roomRepository.findAll();
    }
}
