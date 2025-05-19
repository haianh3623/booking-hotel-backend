package group.assignment.booking_hotel_backend.services.impl;

import group.assignment.booking_hotel_backend.models.Role;
import group.assignment.booking_hotel_backend.models.User;
import group.assignment.booking_hotel_backend.repository.RoleRepository;
import group.assignment.booking_hotel_backend.services.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {
    private RoleRepository roleRepository;
    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role findById(Integer id) {
        Optional<Role> result = roleRepository.findById(id);
        Role role = null;
        if(result.isPresent()){
            role = result.get();
        }
        else{
            throw new RuntimeException("Không thấy role có id: " + id);
        }
        return role;
    }

    @Override
    public void deleteById(Integer Id) {

    }
}
