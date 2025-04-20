package group.assignment.booking_hotel_backend.controller;

import group.assignment.booking_hotel_backend.dto.ReportDto;
import group.assignment.booking_hotel_backend.models.Report;
import group.assignment.booking_hotel_backend.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/{hotelId}/latest")
    public ResponseEntity<ReportDto> getLatestPdfUri(@PathVariable Integer hotelId) {
    Report pdf = reportService.getLatestPdfUriByHotelId(hotelId);
    return pdf != null
        ? ResponseEntity.ok(new ReportDto(pdf.getId(), pdf.getPdfUri(), pdf.getTime().toString()))
        : ResponseEntity.notFound().build();
    }
}
