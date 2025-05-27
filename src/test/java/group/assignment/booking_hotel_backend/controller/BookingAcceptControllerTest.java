package group.assignment.booking_hotel_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import group.assignment.booking_hotel_backend.dto.BookingRequestDto;
import group.assignment.booking_hotel_backend.dto.BookingResponseDto;
import group.assignment.booking_hotel_backend.models.BookingStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingAcceptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Tạo booking mới
    @Test
    public void testCreateBooking_ShouldReturnBookingDetails() throws Exception {
        // Arrange
        BookingRequestDto requestDto = BookingRequestDto.builder()
                .checkIn(LocalDateTime.parse("2025-06-01T12:00:00"))
                .checkOut(LocalDateTime.parse("2025-06-02T12:00:00"))
                .price(500000.0)
                .userId(1)
                .roomId(10)
                .billId(null)
                .build();

        // Act
        String response = mockMvc.perform(post("/api/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        BookingResponseDto responseDto = objectMapper.readValue(response, BookingResponseDto.class);

        // Assert
        assertThat(responseDto.getBookingId()).isNotNull();
        assertThat(responseDto.getPrice()).isEqualTo(500000.0);
        assertThat(responseDto.getStatus()).isEqualTo("PENDING");
        assertThat(responseDto.getUserId()).isEqualTo(1);
        assertThat(responseDto.getRoomId()).isEqualTo(10);
        assertThat(responseDto.getCheckIn()).isEqualTo(requestDto.getCheckIn());
        assertThat(responseDto.getCheckOut()).isEqualTo(requestDto.getCheckOut());
    }


    // Lấy booking chi tiết  theo id
    @Test
    public void testGetBookingById_ShouldReturnBookingDetails() throws Exception {
        int bookingId = 104;
        // Act
        String response = mockMvc.perform(get("/api/booking/" + + bookingId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);

        // Assert
        assertThat(jsonNode.get("bookingId").asInt()).isEqualTo(bookingId);
        assertThat(jsonNode.get("status").asText()).isEqualTo("CONFIRMED");
    }

    // Cập nhật trang thái booking
    @Test
    public void testUpdateBookingStatus_ShouldUpdateStatusAndReturnUpdatedBooking() throws Exception {
        // Arrange
        Integer bookingId = 104;
        BookingStatus newStatus = BookingStatus.CONFIRMED;

        // Act
        String response = mockMvc.perform(put("/api/booking/" + bookingId + "/status")
                        .param("status", newStatus.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Assert
        JsonNode jsonNode = objectMapper.readTree(response);

        assertThat(jsonNode.get("bookingId").asInt()).isEqualTo(bookingId);
        assertThat(jsonNode.get("status").asText()).isEqualTo("CONFIRMED");
    }
}
