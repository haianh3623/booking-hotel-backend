package group.assignment.booking_hotel_backend.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import group.assignment.booking_hotel_backend.models.SearchEntry;
import group.assignment.booking_hotel_backend.utils.TextUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DictionaryService {

    private List<SearchEntry> dictionary;
    private final ObjectMapper mapper = new ObjectMapper();
    private final File dictionaryFile;

    public DictionaryService() {
        try {
            // Đọc file từ resources
            URL resourceUrl = getClass().getClassLoader().getResource("dictionary.json");
            if (resourceUrl == null) throw new RuntimeException("Không tìm thấy dictionary.json");

            dictionaryFile = new File(resourceUrl.toURI());
            dictionary = mapper.readValue(dictionaryFile, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi load dictionary.json", e);
        }
    }

    public List<SearchEntry> getAll() {
        return dictionary;
    }

    public void addEntry(SearchEntry newEntry) {
        try {
            dictionary.add(newEntry);
            mapper.writerWithDefaultPrettyPrinter().writeValue(dictionaryFile, dictionary);
        } catch (Exception e) {
            throw new RuntimeException("Không thể ghi vào dictionary.json", e);
        }
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