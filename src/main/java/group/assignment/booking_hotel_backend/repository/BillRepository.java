package group.assignment.booking_hotel_backend.repository;

import group.assignment.booking_hotel_backend.models.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Integer> {
}
