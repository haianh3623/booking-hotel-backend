package group.assignment.booking_hotel_backend.services;
import group.assignment.booking_hotel_backend.dto.ChangePasswordRequest;
import group.assignment.booking_hotel_backend.dto.RegistrationRequest;
import group.assignment.booking_hotel_backend.dto.UpdateUserRequest;
import group.assignment.booking_hotel_backend.dto.UserDto;
import group.assignment.booking_hotel_backend.models.User;
import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto registerUser(RegistrationRequest request);
    UserDto updateUser(String username, UpdateUserRequest request);
    void changePassword(String username, ChangePasswordRequest request);
    void save(User user);
    List<User> findAll();
    User findById(Integer id);
    void deleteById(Integer Id);
    User findByUsername(String username);
}
