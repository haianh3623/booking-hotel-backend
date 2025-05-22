package group.assignment.booking_hotel_backend.services.impl;

import group.assignment.booking_hotel_backend.dto.*;
import group.assignment.booking_hotel_backend.models.Hotel;
import group.assignment.booking_hotel_backend.models.Room;
import group.assignment.booking_hotel_backend.models.SearchRequest;
import group.assignment.booking_hotel_backend.models.Service;
import group.assignment.booking_hotel_backend.dto.RoomDto;
import group.assignment.booking_hotel_backend.dto.RoomResponseDto;
import group.assignment.booking_hotel_backend.dto.RoomWithRating;
import group.assignment.booking_hotel_backend.mapper.RoomMapper;
import group.assignment.booking_hotel_backend.models.*;
import group.assignment.booking_hotel_backend.repository.*;
import group.assignment.booking_hotel_backend.services.FilesStorageService;
import group.assignment.booking_hotel_backend.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final ServiceRepository serviceRepository;
    private final FilesStorageService filesStorageService;
    private final ReviewServiceImpl reviewService;
    private final AddressRepository addressRepository;
    private final RoomImageRepository roomImageRepository;
    private final ReviewRepository reviewRepository;

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
    public Page<Room> findByHotelId(Integer hotelId, String query, Pageable pageable) {
        return roomRepository.findByHotelIdAndQuery(hotelId, query.toLowerCase(), pageable);
    }

    @Override
    public Long countRoomsByHotelId(Integer hotelId) {
        return roomRepository.countByHotelId(hotelId);
    }

    @Override
    public List<Room> findByHotelId(Integer hotelId) {
        return List.of();
    }

    @Override
    public List<RoomSearchListDto> findRoomByKeyword(String keyword, Pageable pageable) {
        List<RoomSearchListDto> list = roomRepository.searchRoomListByKeyword(keyword);
        for(RoomSearchListDto room : list) {
            Double rating = 0.0;
            Integer reviewCount = 0;
            List<ReviewDto> reviews = reviewService.findByRoomId(room.getRoomId());
            for(ReviewDto review : reviews) {
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

            if (searchRequest.getCity() != null && !searchRequest.getCity().isEmpty() && searchRequest.getCity().compareTo("Tất cả") != 0) {
                List<String> cities = hotelRepository.findCityByHotelName(hotelName);
                if (!cities.contains(searchRequest.getCity())) {
                    System.out.println(!cities.contains(searchRequest.getCity()));
                    iterator.remove();
                    continue;
                }
            }

            if (searchRequest.getDistrict() != null && !searchRequest.getDistrict().isEmpty() && searchRequest.getDistrict().compareTo("Tất cả") != 0) {
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
                if (room.getNumChildrenFree() < searchRequest.getNumOfChild()) {
                    iterator.remove();
                    continue;
                }
            }

            if (searchRequest.getNumOfBed() != null && searchRequest.getNumOfBed() > 0) {
                if (room.getBedNumber() < searchRequest.getNumOfBed()) {
                    iterator.remove();
                }
            }

            if(searchRequest.getServices() != null && !searchRequest.getServices().isEmpty()){
                List<String> roomServices = roomRepository.findServicesByRoomId(room.getRoomId()).stream()
                        .map(Service::getServiceName)
                        .toList();
                for (String service : searchRequest.getServices()) {
                    if (!roomServices.contains(service)) {
                        iterator.remove();
                        break;
                    }
                }
            }

            room.setAddress(addressRepository.findAddressByHotelName(hotelName).toString());

            List<ReviewDto> reviews = reviewRepository.findReviewsByRoomId(room.getRoomId());
            room.setReviewCount(reviews.size());
            room.setRating(calculateAverageRating(reviews));
        }

        boolean isHotelName = false;
        List<String> hotelNames = hotelRepository.getAllHotelNames();
        for (String hotelName : hotelNames) {
            if (searchRequest.getKeyword().equals(hotelName)) {
                isHotelName = true;
                break;
            }
        }

        List<RoomSearchListDto> roomList;

        if (!isHotelName) {
            Map<String, RoomSearchListDto> bestRoomByHotel = new HashMap<>();

            for (RoomSearchListDto room : list) {
                String hotelName = room.getHotelName();

                if (!bestRoomByHotel.containsKey(hotelName)) {
                    bestRoomByHotel.put(hotelName, room);
                } else {
                    RoomSearchListDto existing = bestRoomByHotel.get(hotelName);
                    if (room.getRating() > existing.getRating()) {
                        bestRoomByHotel.put(hotelName, room);
                    }
                }
            }

            roomList = new ArrayList<>(bestRoomByHotel.values());
        } else {
            roomList = list; // hoặc giữ nguyên danh sách nếu đúng là tìm theo tên khách sạn
        }

        roomList.sort(Comparator.comparing(RoomSearchListDto::getRoomId));

        return roomList;
    }

    public double calculateAverageRating(List<ReviewDto> reviews) {
        if (reviews.isEmpty()) {
            return 0.0;
        }
        double totalRating = 0.0;
        for (ReviewDto review : reviews) {
            totalRating += review.getRating();
        }
        return totalRating / reviews.size();
    }

    @Override
    public RoomDetailsDto getRoomDetails(Integer roomId) {
        RoomDetailsDto roomDetails = new RoomDetailsDto();
        Room room = roomRepository.findById(roomId).get();
        if(room != null){
            roomDetails.setRoomId(room.getRoomId());
            roomDetails.setRoomName(room.getRoomName());
            roomDetails.setArea(room.getArea());
            roomDetails.setComboPrice2h(room.getComboPrice2h());
            roomDetails.setPricePerNight(room.getPricePerNight());
            roomDetails.setExtraHourPrice(room.getExtraHourPrice());
            roomDetails.setStandardOccupancy(room.getStandardOccupancy());
            roomDetails.setMaxOccupancy(room.getMaxOccupancy());
            roomDetails.setNumChildrenFree(room.getNumChildrenFree());
            roomDetails.setBedNumber(room.getBedNumber());
            roomDetails.setExtraAdult(room.getExtraAdult());
            roomDetails.setDescription(room.getDescription());

            roomDetails.setRoomImgs(roomImageRepository.findByRoomRoomId(roomId).stream().map(
                    roomImage -> roomImage.getUrl()
            ).toList(
            ));

            roomDetails.setHotelName(hotelRepository.findHotelNameByRoomId(roomId));

            roomDetails.setAddress(addressRepository.findAddressByHotelName(roomDetails.getHotelName()).toString());

            List<Service> services = serviceRepository.findServicesByRoomId(roomId);
            roomDetails.setServices(services.stream().map(Service::getServiceName).toList());

            List<ReviewCardDto> reviews = reviewRepository.findReviewsCardByRoomId(roomId);
            roomDetails.setReviews(reviews);

        }

        return roomDetails;
    }
    @Override
    public List<RoomResponseDto> getTopRatedRooms(int limit) {
        List<Room> allRooms = roomRepository.findAll();

        List<RoomWithRating> roomWithRatings = allRooms.stream().map(room -> {
                    List<Booking> bookings = room.getBookingList();
                    List<Review> allReviews = bookings.stream()
                            .flatMap(booking -> booking.getReviewList().stream())
                            .collect(Collectors.toList());

                    double avgRating = allReviews.stream()
                            .mapToInt(Review::getRating)
                            .average()
                            .orElse(0.0);

                    return new RoomWithRating(room, avgRating);
                }).sorted(Comparator.comparingDouble(RoomWithRating::getAvgRating).reversed())
                .limit(limit)
                .collect(Collectors.toList());

        return roomWithRatings.stream()
                .map(rwr -> RoomMapper.mapToRoomDto(rwr.getRoom(), new RoomResponseDto()))
                .collect(Collectors.toList());
    }

    @Override
    public Long count() {
        return roomRepository.count();
    }

    @Override
    public List<Room> findAllByHotelId(Integer hotelId) {
        return roomRepository.findAllByHotel_HotelId(hotelId);
    }
}
