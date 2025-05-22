package group.assignment.booking_hotel_backend.config;

import group.assignment.booking_hotel_backend.dto.RegistrationRequest;
import group.assignment.booking_hotel_backend.dto.UserDto;
import group.assignment.booking_hotel_backend.mapper.UserMapper;
import group.assignment.booking_hotel_backend.models.Role;
import group.assignment.booking_hotel_backend.models.User;
import group.assignment.booking_hotel_backend.repository.RoleRepository;
import group.assignment.booking_hotel_backend.repository.UserRepository;
import group.assignment.booking_hotel_backend.security.JwtUtil;
import group.assignment.booking_hotel_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestDataService {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private User user;

    public void setUpUser() {
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("test");
        request.setPassword("test");
        request.setFullName("test");
        request.setEmail("test");
        request.setPhone("test");

        user = UserMapper.mapToUser(request, new User(), passwordEncoder);
        Role role = roleRepository.findByName("ROLE_CUSTOMER");
        user.addRole(role);
        User savedUser = userRepository.save(user);
    }

    public void setUpRole() {
        List<Role> roles = Arrays.asList(
                new Role("ROLE_CUSTOMER"),
                new Role("ROLE_ADMIN"),
                new Role("ROLE_HOTEL_OWNER")
        );

        roleRepository.saveAll(roles);
    }

    public String generateCustomerToken() {
        User user = new User();
        user.setUsername("test");
        user.setRoleList(Arrays.asList(new Role("ROLE_CUSTOMER")));
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : user.getRoleList()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities));
    }

    public String generateAdminToken() {
        User user = new User();
        user.setUsername("test");
        user.setRoleList(Arrays.asList(new Role("ROLE_ADMIN")));
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : user.getRoleList()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities));
    }
}
