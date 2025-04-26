package group.assignment.booking_hotel_backend.repository;

import group.assignment.booking_hotel_backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    User findByEmail(String email);
    User findByPhone(String phone);
    List<User> findByRoleList_Name(String roleName);
}