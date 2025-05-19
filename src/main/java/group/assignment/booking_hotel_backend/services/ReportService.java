package group.assignment.booking_hotel_backend.services;

import group.assignment.booking_hotel_backend.models.Report;

import java.util.List;

public interface ReportService {
    List<Report> getReportsByHotelId(Integer hotelId);
    List<String> getPdfUrisByHotelId(Integer hotelId);
    Report getLatestPdfUriByHotelId(Integer hotelId);
}
