package group.assignment.booking_hotel_backend.security;

import group.assignment.booking_hotel_backend.general.dao.UsersDao;
import group.assignment.booking_hotel_backend.general.models.Users;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersDao usersDao;

    public CustomUserDetailsService(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}