package group.assignment.booking_hotel_backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import group.assignment.booking_hotel_backend.models.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findByHotelHotelId(Integer hotelId);

    @Query("SELECT r.pdfUri FROM Report r WHERE r.hotel.hotelId = :hotelId")
    List<String> findPdfUrisByHotelId(@Param("hotelId") Integer hotelId);

    @Query("SELECT r FROM Report r WHERE r.hotel.hotelId = :hotelId ORDER BY r.time DESC LIMIT 1")
    Report findLatestReportByHotelId(@Param("hotelId") Integer hotelId);
}
