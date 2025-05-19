package group.assignment.booking_hotel_backend.services.impl;

import group.assignment.booking_hotel_backend.dto.AddressDto;
import group.assignment.booking_hotel_backend.models.Address;
import group.assignment.booking_hotel_backend.repository.AddressRepository;
import group.assignment.booking_hotel_backend.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;


    @Override
    public List<String> findDistrictByCity(String city) {
        return addressRepository.findDistrictByCity(city);
    }

    @Override
    public List<String> findAllCity() {
        return addressRepository.findAllCity();
    }
}
