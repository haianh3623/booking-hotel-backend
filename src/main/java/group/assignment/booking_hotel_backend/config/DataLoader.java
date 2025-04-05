package group.assignment.booking_hotel_backend.config;

import group.assignment.booking_hotel_backend.models.Role;
import group.assignment.booking_hotel_backend.models.User;
import group.assignment.booking_hotel_backend.repository.RoleRepository;
import group.assignment.booking_hotel_backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        Role customerRole = new Role("ROLE_CUSTOMER");
        Role adminRole = new Role("ROLE_ADMIN");
        Role hotelOwnerRole = new Role("ROLE_HOTEL_OWNER");

        if (roleRepository.findByName("ROLE_CUSTOMER") == null) roleRepository.save(customerRole);
        if (roleRepository.findByName("ROLE_ADMIN") == null) roleRepository.save(adminRole);
        if (roleRepository.findByName("ROLE_HOTEL_OWNER") == null) roleRepository.save(hotelOwnerRole);

        if (userRepository.findByUsername("user") == null) {
            User customer = new User();
            customer.setUsername("user");
                customer.setPassword(passwordEncoder.encode("user"));
            customer.addRole(customerRole);
            userRepository.save(customer);
        }

        if (userRepository.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.addRole(adminRole);
            userRepository.save(admin);
        }

        if (userRepository.findByUsername("owner") == null) {
            User hotelOwner = new User();
            hotelOwner.setUsername("owner");
            hotelOwner.setPassword(passwordEncoder.encode("owner"));
            hotelOwner.addRole(hotelOwnerRole);
            userRepository.save(hotelOwner);
        }
    }
}
