package group.assignment.booking_hotel_backend.services.impl;

import group.assignment.booking_hotel_backend.dto.RoomDto;
import group.assignment.booking_hotel_backend.dto.RoomSearchListDto;
import group.assignment.booking_hotel_backend.models.*;
import group.assignment.booking_hotel_backend.repository.AddressRepository;
import group.assignment.booking_hotel_backend.repository.HotelRepository;
import group.assignment.booking_hotel_backend.repository.RoomRepository;
import group.assignment.booking_hotel_backend.repository.ServiceRepository;
import group.assignment.booking_hotel_backend.services.FilesStorageService;
import group.assignment.booking_hotel_backend.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final ServiceRepository serviceRepository;
    private final FilesStorageService filesStorageService;
    private final ReviewServiceImpl reviewService;
    private final AddressRepository addressRepository;

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

    @Override
    public List<Room> findByHotelId(Integer hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }

    @Override
    public List<RoomSearchListDto> findRoomByKeyword(String keyword, Pageable pageable) {
        List<RoomSearchListDto> list = roomRepository.searchRoomListByKeyword(keyword);
        for(RoomSearchListDto room : list) {
            Double rating = 0.0;
            Integer reviewCount = 0;
            List<Review> reviews = reviewService.findByRoomId(room.getRoomId());
            for(Review review : reviews) {
                rating += review.getRating();
                reviewCount++;
            }
            if (reviewCount > 0) {
                rating /= reviewCount;
            }
            room.setRating(rating);
            room.setReviewCount(reviewCount);
            room.setAddress(addressRepository.findAddressByHotelName(room.getHotelName()).getFirst().toString());
        }


        return list;
    }

    @Override
    public List<RoomSearchListDto> findRoomBySearchRequest(SearchRequest searchRequest, Pageable pageable) {
        List<RoomSearchListDto> list;
        if (!searchRequest.getKeyword().isEmpty()){
            list = findRoomByKeyword(searchRequest.getKeyword(), pageable);
        } else list = roomRepository.findAllRoomList();

        Iterator<RoomSearchListDto> iterator = list.iterator();
        while (iterator.hasNext()) {
            RoomSearchListDto room = iterator.next();
            String hotelName = room.getHotelName();

            if (searchRequest.getCity() != null && !searchRequest.getCity().isEmpty()) {
                List<String> cities = hotelRepository.findCityByHotelName(hotelName);
                if (!cities.contains(searchRequest.getCity())) {
                    System.out.println(!cities.contains(searchRequest.getCity()));
                    iterator.remove();
                    continue;
                }
            }

            if (searchRequest.getDistrict() != null && !searchRequest.getDistrict().isEmpty()) {
                List<String> districts = hotelRepository.findDistrictByHotelName(hotelName);
                if (!districts.contains(searchRequest.getDistrict())) {
                    iterator.remove();
                    continue;
                }
            }

            if (searchRequest.getNumOfAdult() != null && searchRequest.getNumOfAdult() > 0) {
                if (room.getStandardOccupancy() < searchRequest.getNumOfAdult()) {
                    iterator.remove();
                    continue;
                }
            }

            if (searchRequest.getNumOfChild() != null && searchRequest.getNumOfChild() > 0) {
                if (room.getStandardOccupancy() < searchRequest.getNumOfChild()) {
                    iterator.remove();
                    continue;
                }
            }

            if (searchRequest.getNumOfBed() != null && searchRequest.getNumOfBed() > 0) {
                if (room.getBedNumber() < searchRequest.getNumOfBed()) {
                    iterator.remove();
                }
            }
        }



        return list;
    }
}
