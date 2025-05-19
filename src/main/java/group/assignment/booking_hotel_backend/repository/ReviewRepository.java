package group.assignment.booking_hotel_backend.repository;
import group.assignment.booking_hotel_backend.dto.ReviewCardDto;
import group.assignment.booking_hotel_backend.dto.ReviewDto;
import group.assignment.booking_hotel_backend.models.Review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
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

    @Query("""
        SELECT r FROM Review r 
        JOIN r.booking b 
        JOIN b.room ro 
        JOIN ro.hotel h 
        JOIN b.user u
        WHERE h.hotelId = :hotelId 
        AND (:query = '' OR LOWER(u.fullName) LIKE %:query% OR LOWER(r.content) LIKE %:query% OR LOWER(ro.roomName) LIKE %:query%)
        AND (:rating IS NULL OR r.rating = :rating)
        ORDER BY r.createdAt DESC
    """)
    Page<Review> findByHotelIdAndQueryAndRating(
        @Param("hotelId") Integer hotelId,
        @Param("query") String query,
        @Param("rating") Integer rating,
        Pageable pageable
    );
    @Query("SELECT new group.assignment.booking_hotel_backend.dto.ReviewDto" +
            "(r.reviewId, r.content, r.rating, b.bookingId) " +
            "FROM Review r " +
            "JOIN r.booking b " +
            "WHERE b.room.roomId = :roomId")
    List<ReviewDto> findReviewsByRoomId(@Param("roomId") Integer roomId);

    @Query("SELECT new group.assignment.booking_hotel_backend.dto.ReviewCardDto" +
            "(r.reviewId, r.content, r.rating, u.fullName) " +
            "FROM Review r " +
            "JOIN r.booking b " +
            "JOIN b.user u " +
            "WHERE b.room.roomId = :roomId")
    List<ReviewCardDto> findReviewsCardByRoomId(Integer roomId);
}
