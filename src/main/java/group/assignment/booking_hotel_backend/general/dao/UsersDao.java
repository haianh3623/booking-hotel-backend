package group.assignment.booking_hotel_backend.general.dao;

import group.assignment.booking_hotel_backend.general.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersDao extends JpaRepository<Users, Integer> {
    Users findByUsername(String username);
    Users findByEmail(String email);
    Users findByPhone(String phone);
}