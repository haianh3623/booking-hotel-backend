package group.assignment.booking_hotel_backend.repository;
import group.assignment.booking_hotel_backend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
