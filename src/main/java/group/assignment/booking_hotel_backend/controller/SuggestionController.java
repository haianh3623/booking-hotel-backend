package group.assignment.booking_hotel_backend.controller;

import group.assignment.booking_hotel_backend.models.SearchEntry;
import group.assignment.booking_hotel_backend.services.DictionaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SuggestionController {

    private final DictionaryService dictionaryService;

    public SuggestionController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @GetMapping("/search-suggestions")
    public ResponseEntity<List<SearchEntry>> getSuggestions(@RequestParam String query) {
        List<SearchEntry> results = dictionaryService.search(query);
        return ResponseEntity.ok(results);
    }
}
