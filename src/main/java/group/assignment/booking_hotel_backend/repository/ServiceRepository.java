package group.assignment.booking_hotel_backend.repository;
import group.assignment.booking_hotel_backend.models.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Integer> {
}
