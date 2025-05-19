package group.assignment.booking_hotel_backend.services;

import group.assignment.booking_hotel_backend.models.Role;
import group.assignment.booking_hotel_backend.models.User;

import java.util.List;

public interface RoleService {
    Role save(Role role);
    List<Role> findAll();
    Role findById(Integer id);
    void deleteById(Integer Id);
}
