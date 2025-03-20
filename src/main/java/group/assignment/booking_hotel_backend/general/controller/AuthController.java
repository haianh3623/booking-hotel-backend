package group.assignment.booking_hotel_backend.general.controller;

import group.assignment.booking_hotel_backend.general.dto.RegistrationRequest;
import group.assignment.booking_hotel_backend.general.dto.UsersDto;
import group.assignment.booking_hotel_backend.general.security.AuthRequest;
import group.assignment.booking_hotel_backend.general.security.AuthResponse;
import group.assignment.booking_hotel_backend.general.security.JwtUtil;
import group.assignment.booking_hotel_backend.general.services.UsersService;
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
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final group.assignment.booking_hotel_backend.security.CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UsersService usersService;

    public AuthController(AuthenticationManager authenticationManager,
                          group.assignment.booking_hotel_backend.security.CustomUserDetailsService userDetailsService,
                          JwtUtil jwtUtil,
                          UsersService usersService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.usersService = usersService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<UsersDto> registerUser(@RequestBody RegistrationRequest request) {
        UsersDto newUser = usersService.registerUser(request);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
}
