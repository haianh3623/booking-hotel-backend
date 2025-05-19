package group.assignment.booking_hotel_backend.services.impl;

import group.assignment.booking_hotel_backend.repository.ServiceRepository;
import group.assignment.booking_hotel_backend.services.ServiceService;
import lombok.RequiredArgsConstructor;
import group.assignment.booking_hotel_backend.models.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    private final ServiceRepository serviceRepository;

    @Override
    public Service save(Service service) {
        serviceRepository.save(service);
        return service;
    }

    @Override
    public List<Service> findAll() {
        return serviceRepository.findAll();
    }

    @Override
    public Service findById(Integer id) {
        Optional<Service> result = serviceRepository.findById(id);
        Service service = null;
        if(result.isPresent()){
            service = result.get();
        }
        else{
            throw new RuntimeException("Không thấy service có id là: " + id);
        }
        return service;
    }

    @Override
    public void deleteById(Integer id) {
        serviceRepository.deleteById(id);
    }
}