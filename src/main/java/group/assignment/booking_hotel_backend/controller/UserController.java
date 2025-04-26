package group.assignment.booking_hotel_backend.controller;

import group.assignment.booking_hotel_backend.dto.RoleDto;
import group.assignment.booking_hotel_backend.dto.UserDto;
import group.assignment.booking_hotel_backend.dto.UserRequestDto;
import group.assignment.booking_hotel_backend.dto.UserResponseDto;
import group.assignment.booking_hotel_backend.mapper.UserMapper;
import group.assignment.booking_hotel_backend.models.Role;
import group.assignment.booking_hotel_backend.models.User;
import group.assignment.booking_hotel_backend.services.RoleService;
import group.assignment.booking_hotel_backend.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

//    @GetMapping
//    public String hello() {
//        return "Hello, World!";
//    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'HOTEL_OWNER', 'ADMIN')")
    @GetMapping("/profile")
    public ResponseEntity<String> getCustomerProfile() {
        return ResponseEntity.ok("Welcome to customer profile");
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserLogin(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        UserDto userDto = UserMapper.mapToUserDto(user, new UserDto());
        return ResponseEntity.ok(userDto);
    }

//    @PostMapping
//    public UserResponseDto createUser(@RequestBody UserRequestDto userDto) {
//        List<Role> roles = new ArrayList<>();
//        if (userDto != null) {
//            for (RoleDto roleDto : userDto.getRoleDtoList()) {
//                Role role = roleService.findById(roleDto.getRoleId());
//                roles.add(role);
//            }
//        }
//
//        User user = new User();
//        UserMapper.mapToUser(userDto, user, roles);
//        User savedUser = userService.save(user);
//        return UserMapper.mapToUserDto(savedUser, new UserResponseDto());
//    }


    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserResponseDto> userDtos = new ArrayList<>();
        for (User user : users) {
            UserResponseDto userDto = UserMapper.mapToUserDto(user, new UserResponseDto());
            userDtos.add(userDto);
        }
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Integer id) {
        User user = userService.findById(id);
        UserResponseDto userDto = UserMapper.mapToUserDto(user, new UserResponseDto());
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Integer id, @RequestBody UserRequestDto userDto) {
        User user = userService.findById(id);
        List<Role> roles = new ArrayList<>();
        for (RoleDto roleDto : userDto.getRoleDtoList()) {
            Role role = roleService.findById(roleDto.getRoleId());
            roles.add(role);
        }
        User savedUser = UserMapper.mapToUser(userDto, user, roles);
        userService.save(savedUser);
        return ResponseEntity.ok(UserMapper.mapToUserDto(savedUser, new UserResponseDto()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}