package group.assignment.booking_hotel_backend.services;

import group.assignment.booking_hotel_backend.dto.RoomDto;
import group.assignment.booking_hotel_backend.models.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Service
public interface RoomService {
    Room save(RoomDto roomDto);
    Room save(Room room);
    Room update(Integer id, RoomDto roomDto);
    boolean deleteById(Integer id);
    Room findById(Integer id);
    List<Room> findAll();

}