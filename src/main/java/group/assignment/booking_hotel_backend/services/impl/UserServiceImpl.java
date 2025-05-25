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
        // Check if empty
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new UserRegistrationException("Tên tài khoản không được để trống", "username");
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new UserRegistrationException("Email không được để trống", "email");
        }

        if (request.getPhone() == null || request.getPhone().trim().isEmpty()) {
            throw new UserRegistrationException("Số điện thoại không được để trống", "phone");
        }

        if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
            throw new UserRegistrationException("Họ tên không được để trống", "fullName");
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new UserRegistrationException("Mật khẩu không được để trống", "password");
        }
        // Check if username exists
        if (userRepository.findByUsername(request.getUsername()) != null) {
            throw new UserRegistrationException("Tên tài khoản đã tồn tại", "username");
        }

        // Check if email exists
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new UserRegistrationException("Email đã tồn tại", "email");
        }

        // Check if phone exists
        if (userRepository.findByPhone(request.getPhone()) != null) {
            throw new UserRegistrationException("Số điện thoại đã tồn tại", "phone");
        }

        // Validate full name: no digits or special characters
        if (!request.getFullName().matches("^[a-zA-Z\\sÀ-ỹ]+$")) {
            throw new UserRegistrationException("Họ tên không được chứa kí tự đặc biệt hoặc số", "fullName");
        }

        // Validate phone: only digits, 10 characters
        if (!request.getPhone().matches("^\\d{10}$")) {
            throw new UserRegistrationException("Số điện thoại phải chứa chính xác 10 số", "phone");
        }

        // Validate email format
        if (!request.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new UserRegistrationException("Không đúng định dạng email", "email");
        }

        if (request.getPassword().length() < 6) {
            throw new UserRegistrationException("Mật khẩu phải có ít nhất 6 ký tự", "password");
        }

        // Create user and assign role
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
            throw new RuntimeException("Không tìm thấy người dùng");
        }

        // Validate email nếu thay đổi
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (!request.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                throw new UserRegistrationException("Định dạng email không hợp lệ", "email");
            }
            if (userRepository.findByEmail(request.getEmail()) != null) {
                throw new UserRegistrationException("Email đã tồn tại", "email");
            }
            user.setEmail(request.getEmail());
        }

        // Validate phone nếu thay đổi
        if (request.getPhone() != null && !request.getPhone().equals(user.getPhone())) {
            if (!request.getPhone().matches("^\\d{10}$")) {
                throw new UserRegistrationException("Số điện thoại phải đúng 10 chữ số", "phone");
            }
            if (userRepository.findByPhone(request.getPhone()) != null) {
                throw new UserRegistrationException("Số điện thoại đã tồn tại", "phone");
            }
            user.setPhone(request.getPhone());
        }

        // Validate full name nếu thay đổi
        if (request.getFullName() != null) {
            if (!request.getFullName().matches("^[a-zA-Z\\sÀ-ỹ]+$")) {
                throw new UserRegistrationException("Họ tên không được chứa số hoặc ký tự đặc biệt", "fullName");
            }
            user.setFullName(request.getFullName());
        }

        User updatedUser = userRepository.save(user);
        return UserMapper.mapToUserDto(updatedUser, new UserDto());
    }


    @Override
    public void changePassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserRegistrationException("Không tìm thấy người dùng", "username");
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new UserRegistrationException("Mật khẩu cũ không đúng", "oldPassword");
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
