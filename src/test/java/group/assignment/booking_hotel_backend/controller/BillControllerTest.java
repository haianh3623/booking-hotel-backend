package group.assignment.booking_hotel_backend.controller;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import group.assignment.booking_hotel_backend.models.Bill;
import group.assignment.booking_hotel_backend.services.BillService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetBillById_ShouldReturnBillDetails() throws Exception {

        Integer billId = 1;
        // Act
        String response = mockMvc.perform(get("/api/bill/" + billId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Assert
        JsonNode jsonNode = objectMapper.readTree(response);

        assertThat(jsonNode.get("billId").asInt()).isEqualTo(1);
        assertThat(jsonNode.get("paidStatus").asBoolean()).isTrue();
        assertThat(jsonNode.get("userId").asInt()).isEqualTo(1);
    }


    @Test
    public void testChangePaidStatus_ShouldUpdateStatusAndReturnUpdatedBill() throws Exception {
        // Arrange
        Integer billId = 1;
        boolean newPaidStatus = true;

        // Act
        String response = mockMvc.perform(patch("/api/bill/" + billId + "/status")
                        .param("paidStatus", String.valueOf(newPaidStatus))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Assert
        JsonNode jsonNode = objectMapper.readTree(response);

        assertThat(jsonNode.get("billId").asInt()).isEqualTo(1);
        assertThat(jsonNode.get("paidStatus").asBoolean()).isTrue();
        assertThat(jsonNode.get("userId").asInt()).isEqualTo(1);
    }
}
