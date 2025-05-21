package group.assignment.booking_hotel_backend.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class TextUtils {
    public static String removeVietnameseAccents(String input) {
        if (input == null) return null;

        // Normalize Unicode to NFD to separate base and diacritics
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);

        // Remove all combining diacritical marks
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String withoutDiacritics = pattern.matcher(normalized).replaceAll("");

        // Replace đ and Đ manually
        withoutDiacritics = withoutDiacritics.replaceAll("đ", "d").replaceAll("Đ", "D");

        return withoutDiacritics;
    }
}
