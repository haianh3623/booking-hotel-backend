package group.assignment.booking_hotel_backend.services;
import group.assignment.booking_hotel_backend.dto.RoomDetailsDto;
import group.assignment.booking_hotel_backend.dto.RoomDto;
import group.assignment.booking_hotel_backend.dto.RoomResponseDto;
import group.assignment.booking_hotel_backend.dto.RoomSearchListDto;
import group.assignment.booking_hotel_backend.models.Room;
import group.assignment.booking_hotel_backend.models.SearchRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.util.List;
@Service
public interface RoomService {
    Room save(RoomDto roomDto);
    Room save(Room room);
    Room update(Integer id, RoomDto roomDto);
    boolean deleteById(Integer id);
    Room findById(Integer id);
    List<Room> findAll();
    Page<Room> findByHotelId(Integer hotelId, String query, Pageable pageable);
    Long countRoomsByHotelId(Integer hotelId);
    List<Room> findByHotelId(Integer hotelId);

    List<RoomSearchListDto> findRoomByKeyword(String keyword, Pageable pageable);
    List<RoomSearchListDto> findRoomBySearchRequest(SearchRequest searchRequest, Pageable pageable);
    RoomDetailsDto getRoomDetails(Integer roomId);
    List<RoomResponseDto> getTopRatedRooms(int limit);
    Long count();
    List<Room> findAllByHotelId(Integer hotelId);
}