package group.assignment.booking_hotel_backend.controller.confirm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import group.assignment.booking_hotel_backend.dto.BookingRequestDto;
import group.assignment.booking_hotel_backend.dto.BookingResponseDto;
import group.assignment.booking_hotel_backend.dto.UpdateScoreRequest;
import group.assignment.booking_hotel_backend.models.BookingStatus;
import group.assignment.booking_hotel_backend.models.User;
import group.assignment.booking_hotel_backend.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingAcceptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

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

    @Test
    @WithMockUser(username = "user")
    void updateScore_shouldReturnUpdatedUser_withMockService() throws Exception {
        // Arrange
        UpdateScoreRequest request = new UpdateScoreRequest();
        request.setUserId(1);
        request.setScore(2000);

        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setScore(2000);

        Mockito.when(userService.updateScoreByUserId(1, 2000)).thenReturn(mockUser);

        // Act
        String response = mockMvc.perform(put("/api/user/score")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Assert
        JsonNode jsonNode = objectMapper.readTree(response);
        assertThat(jsonNode.get("userId").asInt()).isEqualTo(1);
        assertThat(jsonNode.get("score").asInt()).isEqualTo(2000);
    }


    @Test
    void updateScore_shouldReturnNotFoundIfNotFound() throws Exception {
        // Arrange
        UpdateScoreRequest request = new UpdateScoreRequest();
        request.setUserId(99);
        request.setScore(500);

        // Act & Assert
        mockMvc.perform(put("/api/user/score")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}