package group.assignment.booking_hotel_backend.repository;
import group.assignment.booking_hotel_backend.models.Review;
import group.assignment.booking_hotel_backend.models.Service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;



    public interface ServiceRepository extends JpaRepository<Service, Integer> {
        Optional<Service> findByServiceName(String name);
        List<Service> findTop5ByOrderByServiceIdAsc();

    
}
