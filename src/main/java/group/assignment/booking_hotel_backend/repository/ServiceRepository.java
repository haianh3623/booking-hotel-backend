package group.assignment.booking_hotel_backend.repository;
import group.assignment.booking_hotel_backend.models.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Integer> {
    Optional<Service> findByServiceName(String name);
    List<Service> findTop5ByOrderByServiceIdAsc();
}
