package group.assignment.booking_hotel_backend.services;


import group.assignment.booking_hotel_backend.models.Service;

import java.util.List;

@org.springframework.stereotype.Service
public interface ServiceService {
    Service save(Service service);
    List<Service> findAll();
    Service findById(Integer theId);
    void deleteById(Integer theId);
}