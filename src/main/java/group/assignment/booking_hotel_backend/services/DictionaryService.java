package group.assignment.booking_hotel_backend.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import group.assignment.booking_hotel_backend.models.SearchEntry;
import group.assignment.booking_hotel_backend.repository.AddressRepository;
import group.assignment.booking_hotel_backend.repository.HotelRepository;
import group.assignment.booking_hotel_backend.repository.RoomRepository;
import group.assignment.booking_hotel_backend.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DictionaryService {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private RoomRepository roomRepository;

    private final List<SearchEntry> dictionary = new ArrayList<>();
    private final ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        try {
            List<String> newValues = new ArrayList<>();
            newValues.addAll(fetchCityNames());
            newValues.addAll(fetchDistrictNames());
            newValues.addAll(fetchHotelNames());
            newValues.addAll(fetchRoomNames());

            for (String value : newValues) {
                boolean exists = dictionary.stream()
                        .anyMatch(entry -> entry.getValue().equalsIgnoreCase(value));
                if (!exists) {
                    dictionary.add(new SearchEntry("none", value));
                }
            }

            System.out.println("Dictionary initialized with " + dictionary.size() + " entries.");

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi khởi tạo dictionary", e);
        }
    }
    private List<String> fetchCityNames() {
        return addressRepository.findAllCity();
    }

    private List<String> fetchDistrictNames() {
        return addressRepository.findAllDistrict();
    }

    private List<String> fetchHotelNames() {
        return hotelRepository.getAllHotelNames();
    }

    private List<String> fetchRoomNames() {
        return roomRepository.findDistinctRoomNames();
    }

    public List<SearchEntry> search(String query) {
        String normalizedQuery = TextUtils.removeVietnameseAccents(query.toLowerCase());

        return dictionary.stream()
                .filter(entry -> {
                    String normalizedValue = TextUtils.removeVietnameseAccents(entry.getValue().toLowerCase());
                    return normalizedValue.contains(normalizedQuery);
                })
                .limit(10)
                .collect(Collectors.toList());
    }
}
