package group.assignment.booking_hotel_backend.controller;

import group.assignment.booking_hotel_backend.dto.ServiceDto;
import group.assignment.booking_hotel_backend.mapper.ServiceMapper;
import group.assignment.booking_hotel_backend.models.Service;
import group.assignment.booking_hotel_backend.services.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing services
 */
@RestController
@RequestMapping("/api/service")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceService serviceService;

    /**
     * Get all services
     * @return List of all services
     */
    @GetMapping("")
    public ResponseEntity<List<ServiceDto>> getAllServices() {
        try {
            List<ServiceDto> serviceList = serviceService.findAll().stream()
                    .map(service -> ServiceMapper.mapToServiceDto(service, new ServiceDto()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(serviceList);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    
    /**
     * Create a new service
     * @param serviceDto Service data
     * @return Created service
     */
    @PostMapping("")
    public ResponseEntity<ServiceDto> createService(@RequestBody ServiceDto serviceDto) {
        try {
            Service service = new Service();
            service.setServiceName(serviceDto.getServiceName());
            service.setPrice(serviceDto.getPrice());
            
            Service savedService = serviceService.save(service);
            return ResponseEntity.ok(ServiceMapper.mapToServiceDto(savedService, new ServiceDto()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    
    /**
     * Delete a service
     * @param id Service ID to delete
     * @return Success status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Integer id) {
        try {
            serviceService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}