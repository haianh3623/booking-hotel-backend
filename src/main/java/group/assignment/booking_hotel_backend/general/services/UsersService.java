package group.assignment.booking_hotel_backend.general.services;

import group.assignment.booking_hotel_backend.general.dao.UsersDao;
import group.assignment.booking_hotel_backend.general.dto.RegistrationRequest;
import group.assignment.booking_hotel_backend.general.dto.UsersDto;
import group.assignment.booking_hotel_backend.general.models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsersService {

    private final UsersDao usersDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(UsersDao usersDao, PasswordEncoder passwordEncoder) {
        this.usersDao = usersDao;
        this.passwordEncoder = passwordEncoder;
    }

    public UsersDto createUser(UsersDto userDto) {
        Users user = new Users();
        user.setUsername(userDto.getUsername());
        user.setFullname(userDto.getFullname());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setScore(0);

        Users savedUser = usersDao.save(user);
        return new UsersDto(savedUser);
    }

    public UsersDto registerUser(RegistrationRequest request) {
        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setFullname(request.getFullname());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setScore(0);

        Users savedUser = usersDao.save(user);
        return new UsersDto(savedUser);
    }
}