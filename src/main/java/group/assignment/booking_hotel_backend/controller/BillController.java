package group.assignment.booking_hotel_backend.controller;

import group.assignment.booking_hotel_backend.dto.BillResponseDto;
import group.assignment.booking_hotel_backend.mapper.BillMapper;
import group.assignment.booking_hotel_backend.models.Bill;
import group.assignment.booking_hotel_backend.services.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/bill")
@RequiredArgsConstructor
public class BillController {
    private final BillService billService;

    @GetMapping
    public ResponseEntity<List<BillResponseDto>> getAllBills() {
        List<Bill> bills = billService.findAll();
        List<BillResponseDto> billResponseDtoList = new ArrayList<>();
        for (Bill bill : bills) {
            billResponseDtoList.add(BillMapper.mapToBillResponseDto(bill, new BillResponseDto()));
        }
        return ResponseEntity.ok(billResponseDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillResponseDto> getBillById(@PathVariable Integer id) {
        Bill bill = billService.findById(id);
        if (bill != null) {
            return ResponseEntity.ok(BillMapper.mapToBillResponseDto(bill, new BillResponseDto()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    public ResponseEntity<BillResponseDto> createBill(@RequestBody Bill bill) {
        return ResponseEntity.ok(BillMapper.mapToBillResponseDto(billService.save(bill), new BillResponseDto()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillResponseDto> updateBill(@PathVariable Integer id, @RequestBody Bill bill) {
        return ResponseEntity.ok(BillMapper.mapToBillResponseDto(billService.update(id, bill), new BillResponseDto()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Integer id) {
        billService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BillResponseDto> changePaidStatus(@PathVariable Integer id, @RequestParam boolean paidStatus) {
        Bill bill = billService.findById(id);
        if (bill == null) {
            return ResponseEntity.notFound().build();
        }
        bill.setPaidStatus(paidStatus);
        return ResponseEntity.ok(BillMapper.mapToBillResponseDto(billService.save(bill), new BillResponseDto()));
    }
}
