package group.assignment.booking_hotel_backend.controller;

import group.assignment.booking_hotel_backend.dto.AddressDto;
import group.assignment.booking_hotel_backend.services.impl.AddressServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;
import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    AddressServiceImpl addressService;

    @GetMapping("/city")
    public ResponseEntity<List<String>> getAllCity() {
        List<String> cities = addressService.findAllCity();
        return ResponseEntity.ok(cities);
    }
    @GetMapping("/district")
    public ResponseEntity<List<String>> getDistrictByCity(@RequestParam String city) {
        List<String> districts = addressService.findDistrictByCity(city);
        return ResponseEntity.ok(districts);
    }
}
