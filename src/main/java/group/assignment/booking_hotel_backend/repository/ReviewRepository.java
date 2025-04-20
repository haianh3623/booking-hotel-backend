package group.assignment.booking_hotel_backend.repository;
import group.assignment.booking_hotel_backend.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByBookingBookingId(Integer bookingId);

    @Query("SELECT COUNT(r) FROM Review r " +
       "JOIN r.booking b " +
       "JOIN b.room ro " +
       "JOIN ro.hotel h " +
       "WHERE h.hotelId = :hotelId AND MONTH(r.createdAt) = :month AND YEAR(r.createdAt) = :year")
    Long countReviewsByHotelIdAndMonth(
        @Param("hotelId") Integer hotelId,
        @Param("month") int month,
        @Param("year") int year
    );

    @Query("""
        SELECT AVG(r.rating) FROM Review r 
        JOIN r.booking b 
        JOIN b.room ro 
        WHERE ro.hotel.hotelId = :hotelId 
        AND MONTH(r.createdAt) = :month 
        AND YEAR(r.createdAt) = :year
    """)
    Double averageRatingByHotelAndMonth(
        @Param("hotelId") Integer hotelId,
        @Param("month") int month,
        @Param("year") int year
    );

}
