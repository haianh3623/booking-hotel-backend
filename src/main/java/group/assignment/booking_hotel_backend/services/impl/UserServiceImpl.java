package group.assignment.booking_hotel_backend.services.impl;
import group.assignment.booking_hotel_backend.dto.RegistrationRequest;
import group.assignment.booking_hotel_backend.dto.UserDto;
import group.assignment.booking_hotel_backend.dto.UpdateUserRequest;
import group.assignment.booking_hotel_backend.dto.ChangePasswordRequest;
import group.assignment.booking_hotel_backend.mapper.UserMapper;
import group.assignment.booking_hotel_backend.models.Role;
import group.assignment.booking_hotel_backend.models.User;
import group.assignment.booking_hotel_backend.repository.RoleRepository;
import group.assignment.booking_hotel_backend.repository.UserRepository;
import group.assignment.booking_hotel_backend.services.UserService;
import group.assignment.booking_hotel_backend.exception.UserRegistrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto, new User());
        User savedUser = userRepository.save(user);
        return UserMapper.mapToUserDto(savedUser, new UserDto());
    }

    @Override
    public UserDto registerUser(RegistrationRequest request) {
        if (userRepository.findByUsername(request.getUsername()) != null) {
            throw new UserRegistrationException("Username already exists", "username");
        }

        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new UserRegistrationException("Email already exists", "email");
        }

        if (userRepository.findByPhone(request.getPhone()) != null) {
            throw new UserRegistrationException("Phone number already exists", "phone");
        }

        User user = UserMapper.mapToUser(request, new User(), passwordEncoder);
        Role role = roleRepository.findByName("ROLE_CUSTOMER");
        user.addRole(role);
        User savedUser = userRepository.save(user);
        return UserMapper.mapToUserDto(savedUser, new UserDto());
    }

    @Override
    public UserDto updateUser(String username, UpdateUserRequest request) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.findByEmail(request.getEmail()) != null) {
                throw new RuntimeException("Email already exists");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getPhone() != null && !request.getPhone().equals(user.getPhone())) {
            if (userRepository.findByPhone(request.getPhone()) != null) {
                throw new RuntimeException("Phone number already exists");
            }
            user.setPhone(request.getPhone());
        }

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }

        User updatedUser = userRepository.save(user);
        return UserMapper.mapToUserDto(updatedUser, new UserDto());
    }

    @Override
    public void changePassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Integer id) {
        Optional<User> result = userRepository.findById(id);
        User user = null;
        if(result.isPresent()){
            user = result.get();
        }
        else{
            throw new RuntimeException("Không thấy user có id: " + id);
        }
        return user;
    }

    @Override
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Long count() {
        return userRepository.count();
    }

    @Override
    public List<User> findByRoleList_Name(String roleName) {
        return userRepository.findByRoleList_Name(roleName);
    }
}
