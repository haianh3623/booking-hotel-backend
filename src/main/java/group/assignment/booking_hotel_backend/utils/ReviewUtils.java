package group.assignment.booking_hotel_backend.utils;

public class ReviewUtils {
    public static Double calculateChangePercentage(Long previous, Long current) {
        if (previous == 0) {
            return current > 0 ? 100.0 : 0.0;
        }
        return ((double)(current - previous) / previous) * 100.0;
    }
}
