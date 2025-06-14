package group.assignment.booking_hotel_backend.impl;

import group.assignment.booking_hotel_backend.models.Bill;
import group.assignment.booking_hotel_backend.models.User;
import group.assignment.booking_hotel_backend.repository.BillRepository;
import group.assignment.booking_hotel_backend.services.impl.BillServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillServiceImplTest {

    @Mock
    private BillRepository billRepository;

    @InjectMocks
    private BillServiceImpl billService;

    @Test
    void findById_shouldReturnBill_whenBillExists() {
        // Arrange
        Bill bill = new Bill();
        bill.setBillId(1);
        bill.setTotalPrice(500000.0);
        bill.setPaidStatus(false);
        bill.setUser(new User());

        when(billRepository.findById(1)).thenReturn(Optional.of(bill));

        // Act
        Bill result = billService.findById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getBillId());
        assertEquals(500000.0, result.getTotalPrice());
        assertFalse(result.getPaidStatus());
        verify(billRepository).findById(1);
    }

    @Test
    void findById_shouldThrowException_whenBillNotFound() {
        // Arrange
        when(billRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> billService.findById(99));
        assertEquals("Không thấy bill có id là: 99", exception.getMessage());
        verify(billRepository).findById(99);
    }

    @Test
    void save_shouldCallRepositoryAndReturnSavedBill() {
        // Arrange
        Bill bill = Bill.builder()
                .billId(1)
                .totalPrice(500000.0)
                .paidStatus(false)
                .build();

        when(billRepository.save(bill)).thenReturn(bill);

        // Act
        Bill result = billService.save(bill);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getBillId());
        assertEquals(500000.0, result.getTotalPrice());
        verify(billRepository).save(bill);
    }
}
