package group.assignment.booking_hotel_backend.controller;
import group.assignment.booking_hotel_backend.dto.RegistrationRequest;
import group.assignment.booking_hotel_backend.dto.UserDto;
import group.assignment.booking_hotel_backend.security.AuthRequest;
import group.assignment.booking_hotel_backend.security.AuthResponse;
import group.assignment.booking_hotel_backend.security.JwtUtil;
import group.assignment.booking_hotel_backend.services.UserService;
import group.assignment.booking_hotel_backend.models.User;
import group.assignment.booking_hotel_backend.models.Role;
import group.assignment.booking_hotel_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final group.assignment.booking_hotel_backend.security.CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
            final String jwt = jwtUtil.generateToken(userDetails);

            User user = userRepository.findByUsername(authRequest.getUsername());
            String[] roles = user.getRoleList().stream()
                    .map(Role::getName)
                    .toArray(String[]::new);

            return ResponseEntity.ok(new AuthResponse(jwt, user.getUserId(), roles));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
    }


    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody RegistrationRequest request) {
        UserDto newUser = userService.registerUser(request);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
}
