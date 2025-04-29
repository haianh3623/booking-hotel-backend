package group.assignment.booking_hotel_backend.services.impl;
import group.assignment.booking_hotel_backend.models.Report;
import group.assignment.booking_hotel_backend.repository.ReportRepository;
import group.assignment.booking_hotel_backend.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    @Override
    public List<Report> getReportsByHotelId(Integer hotelId) {
        return reportRepository.findByHotelHotelId(hotelId);
    }

    @Override
    public List<String> getPdfUrisByHotelId(Integer hotelId) {
        return reportRepository.findPdfUrisByHotelId(hotelId);
    }

    @Override
    public Report getLatestPdfUriByHotelId(Integer hotelId) {
        Report latestReport = reportRepository.findLatestReportByHotelId(hotelId);
        return latestReport != null ? latestReport : null;
    }
}
