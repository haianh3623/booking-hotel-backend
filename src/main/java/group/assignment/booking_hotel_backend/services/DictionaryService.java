package group.assignment.booking_hotel_backend.services;

import group.assignment.booking_hotel_backend.models.SearchEntry;
import group.assignment.booking_hotel_backend.utils.TextUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DictionaryService {

    private final List<SearchEntry> dictionary;

    public DictionaryService() {
        dictionary = List.of(
                new SearchEntry("location", "Đà Nẵng"),
                new SearchEntry("location", "Hà Nội"),
                new SearchEntry("hotel", "Vinpearl Resort"),
                new SearchEntry("hotel", "Fusion Suites"),
                new SearchEntry("amenity", "WiFi miễn phí"),
                new SearchEntry("amenity", "Hồ bơi"),
                new SearchEntry("amenity", "Bữa sáng miễn phí")
        );
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