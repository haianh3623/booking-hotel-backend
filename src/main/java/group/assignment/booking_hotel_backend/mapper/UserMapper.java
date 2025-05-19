package group.assignment.booking_hotel_backend.mapper;
import group.assignment.booking_hotel_backend.dto.*;
import group.assignment.booking_hotel_backend.models.Role;
import group.assignment.booking_hotel_backend.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

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

    public static UserResponseDto mapToUserDto(User user, UserResponseDto dto) {
        dto.setUserId(user.getUserId());
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setScore(user.getScore());

        if (user.getRoleList() != null) {
            List<RoleDto> roleDtos = new ArrayList<>();
            for (Role role : user.getRoleList()) {
                roleDtos.add(RoleMapper.mapToRoleDto(role, new RoleDto()));
            }

            dto.setRoleDtoList(roleDtos);
        }
        return dto;
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

    public static User mapToUser(UserRequestDto dto, User user, List<Role> roles) {
        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setScore(dto.getScore());
        user.setRoleList(roles);
        return user;
    }
}
