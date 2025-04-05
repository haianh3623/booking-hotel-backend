package group.assignment.booking_hotel_backend.mapper;
import group.assignment.booking_hotel_backend.dto.RegistrationRequest;
import group.assignment.booking_hotel_backend.dto.UserDto;
import group.assignment.booking_hotel_backend.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper {

    public static UserDto mapToUserDto(User user, UserDto userDto) {
        userDto.setUserId(user.getUserId());
        userDto.setUsername(user.getUsername());
        userDto.setFullName(user.getFullName());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setPassword(user.getPassword());
        userDto.setScore(user.getScore());
        return userDto;
    }

    public static User mapToUser(UserDto userDto, User user) {
        user.setUsername(userDto.getUsername());
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setPassword(userDto.getPassword());
        user.setScore(userDto.getScore());
        return user;
    }

    public static User mapToUser(RegistrationRequest request, User user, PasswordEncoder passwordEncoder) {
        user.setUsername(request.getUsername());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setScore(0);
        return user;
    }
}
