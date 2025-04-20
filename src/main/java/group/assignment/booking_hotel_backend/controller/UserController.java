package group.assignment.booking_hotel_backend.controller;

import group.assignment.booking_hotel_backend.dto.UserDto;
import group.assignment.booking_hotel_backend.mapper.UserMapper;
import group.assignment.booking_hotel_backend.models.User;
import group.assignment.booking_hotel_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String hello() {
        return "Hello, World!";
    }

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
}