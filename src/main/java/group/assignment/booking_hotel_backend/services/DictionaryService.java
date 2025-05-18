package group.assignment.booking_hotel_backend.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import group.assignment.booking_hotel_backend.models.SearchEntry;
import group.assignment.booking_hotel_backend.utils.TextUtils;
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

    private List<SearchEntry> dictionary = new ArrayList<>();
    private final ObjectMapper mapper = new ObjectMapper();

    // Đường dẫn tạm để lưu lại file nếu cần ghi
    private File dictionaryFile;

    @PostConstruct
    public void init() {
        try {
            // Load file từ resources (classpath)
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dictionary.json");
            if (inputStream == null) {
                throw new RuntimeException("Không tìm thấy dictionary.json trong resources.");
            }

            // Đọc nội dung file
            dictionary = mapper.readValue(inputStream, new TypeReference<>() {});

            // Ghi file ra temp để có thể cập nhật lại
            Path tempFile = Files.createTempFile("dictionary", ".json");
            dictionaryFile = tempFile.toFile();
            mapper.writerWithDefaultPrettyPrinter().writeValue(dictionaryFile, dictionary);

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

            // Ghi lại vào file tạm đã lưu
            if (dictionaryFile != null) {
                mapper.writerWithDefaultPrettyPrinter().writeValue(dictionaryFile, dictionary);
            }

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
