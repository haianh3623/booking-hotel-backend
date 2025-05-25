package group.assignment.booking_hotel_backend.controller.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import group.assignment.booking_hotel_backend.config.TestDataService;
import group.assignment.booking_hotel_backend.dto.BookingRequestDto;
import group.assignment.booking_hotel_backend.dto.BookingResponseDto;
import group.assignment.booking_hotel_backend.dto.BookingSearchRequest;
import group.assignment.booking_hotel_backend.models.Booking;
import group.assignment.booking_hotel_backend.models.BookingStatus;
import group.assignment.booking_hotel_backend.repository.RoleRepository;
import group.assignment.booking_hotel_backend.repository.UserRepository;
import group.assignment.booking_hotel_backend.security.JwtUtil;
import group.assignment.booking_hotel_backend.services.BookingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerTestExample {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private BookingService bookingService;


//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private TestDataService testDataService;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private RoleRepository roleRepository;

//    @BeforeEach
//    public void setUp() {
////        testDataService.setUpUser();
////        testDataService.setUpRole();
//    }
//
//    @AfterEach
//    public void tearDown() {
////        userRepository.deleteAll();
////        roleRepository.deleteAll();
//    }

//    @Test
//    public void testSearchHotels_WithValidData_ShouldReturnHotels() throws Exception {
//        // AAA pattern
//        // Arrange
//        // Act
//        // Assert
//        BookingSearchRequest searchRequest = BookingSearchRequest.builder()
//                .infoSearch("")
//                .city("Hanoi")
//                .district("Hoan Kiem")
//                .checkInDate("2024-04-01")
//                .checkOutDate("2024-04-05")
//                .checkInTime("14:00")
//                .checkOutTime("12:00")
//                .adults(2)
//                .children(1)
//                .bedNumber(2)
//                .priceFrom(10.0)
//                .priceTo(30000000.0)
//                .sortBy("price_asc")
//                .services(List.of("wifi"))
//                .build();
//
//        String response = mockMvc.perform(post("/api/booking/search")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(searchRequest)))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        JsonNode jsonNode = objectMapper.readTree(response);
//
//        assertThat(jsonNode.size()).isGreaterThan(0);
//        assertThat(jsonNode.get(0).get("roomId").asLong()).isEqualTo(2);
//    }


    // Kiểm tra lựa chọn khu vực "Tất cả"
    @Test
    public void testSearchRoom_SelectAllAreas_ShouldReturnAllCities() throws Exception {
        // Arrange
        BookingSearchRequest searchRequest = BookingSearchRequest.builder()
                .infoSearch("")
                .city("")
                .district("")
                .checkInDate("2025-06-01")
                .checkOutDate("2025-06-02")
                .checkInTime("14:00")
                .checkOutTime("12:00")
                .adults(2)
                .children(1)
                .bedNumber(1)
                .priceFrom(0.0)
                .priceTo(500000.0)
                .sortBy("price_asc")
                .services(List.of())
                .build();

        // Act
        String response = mockMvc.perform(post("/api/booking/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);

        // Assert
        assertThat(jsonNode.size()).isGreaterThan(0);

        boolean isFound = false;
        for (JsonNode node : jsonNode) {
            if (node.get("roomName").asText().equals("DeluxeTest Room") &&
                    node.get("hotelName").asText().equals("Grand Hotel Hanoi")) {
                isFound = true;
                break;
            }
        }
        assertThat(isFound).isTrue();
    }


    // Kiểm tra lựa chọn khu vực cụ thể (Hà Nội)
    @Test
    public void testSearchRoom_SelectSpecificCity_ShouldReturnCorrectCities() throws Exception {
        // Arrange
        BookingSearchRequest searchRequest = BookingSearchRequest.builder()
                .infoSearch("")
                .city("Hanoi")
                .district("")
                .checkInDate("2025-06-01")
                .checkOutDate("2025-06-02")
                .checkInTime("14:00")
                .checkOutTime("12:00")
                .adults(2)
                .children(1)
                .bedNumber(1)
                .priceFrom(0.0)
                .priceTo(500000.0)
                .sortBy("price_asc")
                .services(List.of())
                .build();

        // Act
        String response = mockMvc.perform(post("/api/booking/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);

        // Assert
        boolean isFound = false;
        for (JsonNode node : jsonNode) {
            if (node.get("roomName").asText().equals("DeluxeTest Room") &&
                    node.get("hotelName").asText().equals("Grand Hotel Hanoi")) {
                isFound = true;
                break;
            }
        }
        assertThat(isFound).isTrue();
    }


    //  Kiểm tra lựa chọn quận "Tất cả"
    @Test
    public void testSearchRoom_SelectAllDistricts_ShouldReturnAllRooms() throws Exception {
        // Arrange
        BookingSearchRequest searchRequest = BookingSearchRequest.builder()
                .infoSearch("")
                .city("")
                .district("")
                .checkInDate("2025-06-01")
                .checkOutDate("2025-06-02")
                .checkInTime("14:00")
                .checkOutTime("12:00")
                .adults(2)
                .children(1)
                .bedNumber(1)
                .priceFrom(0.0)
                .priceTo(500000.0)
                .sortBy("price_asc")
                .services(List.of())
                .build();

        // Act
        String response = mockMvc.perform(post("/api/booking/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);

        // Assert
        boolean isFound = false;
        for (JsonNode node : jsonNode) {
            if (node.get("roomName").asText().equals("DeluxeTest Room") &&
                    node.get("hotelName").asText().equals("Grand Hotel Hanoi")) {
                isFound = true;
                break;
            }
        }
        assertThat(isFound).isTrue();
    }

    //  Kiểm tra lựa chọn quận cụ thể (Hoàn Kiếm)
    @Test
    public void testSearchRoom_SelectSpecificDistrict_ShouldReturnCorrectRooms() throws Exception {
        // Arrange
        BookingSearchRequest searchRequest = BookingSearchRequest.builder()
                .infoSearch("")
                .city("")
                .district("Hoan Kiem")
                .checkInDate("2025-06-01")
                .checkOutDate("2025-06-02")
                .checkInTime("14:00")
                .checkOutTime("12:00")
                .adults(2)
                .children(1)
                .bedNumber(1)
                .priceFrom(0.0)
                .priceTo(500000.0)
                .sortBy("price_asc")
                .services(List.of())
                .build();

        // Act
        String response = mockMvc.perform(post("/api/booking/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);

        // Assert
        boolean isFound = false;
        for (JsonNode node : jsonNode) {
            if (node.get("roomName").asText().equals("DeluxeTest Room") &&
                    node.get("hotelName").asText().equals("Grand Hotel Hanoi")) {
                isFound = true;
                break;
            }
        }
        assertThat(isFound).isTrue();
    }



    // Kiểm tra đặt dưới 2h, không vượt số người lớn tiêu chuẩn, số trẻ em bằng số trẻ em miễn phí
    @Test
    public void testBooking_Under2Hours_WithinAdultLimit_EqualFreeChildren_ShouldReturnCombo2hPrice() throws Exception {
        BookingSearchRequest searchRequest = BookingSearchRequest.builder()
                .infoSearch("DeluxeTest Room")
                .city("Hanoi")
                .district("Hoan Kiem")
                .checkInDate("2024-06-05")
                .checkOutDate("2024-06-05")
                .checkInTime("12:00")
                .checkOutTime("13:00")
                .adults(2)
                .children(1)
                .bedNumber(1)
                .priceFrom(0.0)
                .priceTo(600000.0)
                .sortBy("price_asc")
                .services(List.of())
                .build();

        String response = mockMvc.perform(post("/api/booking/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        JsonNode foundNode = null;
        for (JsonNode node : jsonNode) {
            if (node.get("roomName").asText().equals("DeluxeTest Room") &&
                    node.get("hotelName").asText().equals("Grand Hotel Hanoi")) {
                foundNode = node;
                break;
            }
        }
        assertThat(foundNode).isNotNull();
        assertThat(foundNode.get("price").asDouble()).isEqualTo(150000);
        System.out.println("Found room: " + foundNode);
    }

    // Kiểm tra đặt dưới 2h, vượt số người lớn tiêu chuẩn, số trẻ em bằng số trẻ em miễn phí
    @Test
    public void testBooking_Under2Hours_ExceedAdultLimit_EqualFreeChildren_ShouldIncludeExtraAdultFee() throws Exception {
        BookingSearchRequest searchRequest = BookingSearchRequest.builder()
                .infoSearch("DeluxeTest Room")
                .city("Hanoi")
                .district("Hoan Kiem")
                .checkInDate("2024-06-05")
                .checkOutDate("2024-06-05")
                .checkInTime("12:00")
                .checkOutTime("13:00")
                .adults(3)
                .children(1)
                .bedNumber(1)
                .priceFrom(0.0)
                .priceTo(600000.0)
                .sortBy("price_asc")
                .services(List.of())
                .build();

        String response = mockMvc.perform(post("/api/booking/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        JsonNode foundNode = null;
        for (JsonNode node : jsonNode) {
            if (node.get("roomName").asText().equals("DeluxeTest Room") &&
                    node.get("hotelName").asText().equals("Grand Hotel Hanoi")) {
                foundNode = node;
                break;
            }
        }
        assertThat(foundNode).isNotNull();
        assertThat(foundNode.get("price").asDouble()).isEqualTo(250000);
        System.out.println("Found room: " + foundNode);
    }

    // Kiểm tra đặt dưới 2h, không vượt số người lớn tiêu chuẩn, số trẻ em lớn hơn số trẻ em miễn phí
    @Test
    public void testBooking_Under2Hours_WithinAdultLimit_ExceedFreeChildren_ShouldIncludeExtraChildFee() throws Exception {
        BookingSearchRequest searchRequest = BookingSearchRequest.builder()
                .infoSearch("DeluxeTest Room")
                .city("Hanoi")
                .district("Hoan Kiem")
                .checkInDate("2024-06-05")
                .checkOutDate("2024-06-05")
                .checkInTime("12:00")
                .checkOutTime("13:00")
                .adults(2)
                .children(2)
                .bedNumber(1)
                .priceFrom(0.0)
                .priceTo(600000.0)
                .sortBy("price_asc")
                .services(List.of())
                .build();

        String response = mockMvc.perform(post("/api/booking/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        JsonNode foundNode = null;
        for (JsonNode node : jsonNode) {
            if (node.get("roomName").asText().equals("DeluxeTest Room") &&
                    node.get("hotelName").asText().equals("Grand Hotel Hanoi")) {
                foundNode = node;
                break;
            }
        }
        assertThat(foundNode).isNotNull();
        assertThat(foundNode.get("price").asDouble()).isEqualTo(250000);
        System.out.println("Found room: " + foundNode);
    }

    // Kiểm tra đặt trên 2h, giá tính theo ngày đêm rẻ hơn giá giờ, không vượt số người lớn tiêu chuẩn, số trẻ em bằng số trẻ em miễn phí
    // Fail
    @Test
    public void testBooking_Over2Hours_DayPriceCheaperThanHourly_WithinAdultLimit_EqualFreeChildren_ShouldReturnDayPrice() throws Exception {
        BookingSearchRequest searchRequest = BookingSearchRequest.builder()
                .infoSearch("DeluxeTest Room")
                .city("Hanoi")
                .district("Hoan Kiem")
                .checkInDate("2024-06-05")
                .checkOutDate("2024-06-05")
                .checkInTime("14:00")
                .checkOutTime("19:00")
                .adults(2)
                .children(1)
                .bedNumber(1)
                .priceFrom(0.0)
                .priceTo(600000.0)
                .sortBy("price_asc")
                .services(List.of())
                .build();

        String response = mockMvc.perform(post("/api/booking/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);

        JsonNode foundNode = null;
        for (JsonNode node : jsonNode) {
            if (node.get("roomName").asText().equals("DeluxeTest Room") &&
                    node.get("hotelName").asText().equals("Grand Hotel Hanoi")) {
                foundNode = node;
                break;
            }
        }

        assertThat(foundNode).isNotNull();
        assertThat(foundNode.get("price").asDouble()).isEqualTo(400000);
        System.out.println("Found room: " + foundNode);
    }

    // Kiểm tra đặt trên 2h, giá giờ rẻ hơn giá ngày đêm, không vượt số người lớn tiêu chuẩn, số trẻ em bằng số trẻ em miễn phí
    @Test
    public void testBooking_Over2Hours_HourlyPriceCheaper_WithinAdultLimit_EqualFreeChildren_ShouldApplyHourlyPrice() throws Exception {
        BookingSearchRequest searchRequest = BookingSearchRequest.builder()
                .infoSearch("DeluxeTest Room")
                .city("Hanoi")
                .district("Hoan Kiem")
                .checkInDate("2024-06-05")
                .checkOutDate("2024-06-05")
                .checkInTime("14:00")
                .checkOutTime("17:00")
                .adults(2)
                .children(1)
                .bedNumber(1)
                .priceFrom(0.0)
                .priceTo(600000.0)
                .sortBy("price_asc")
                .services(List.of())
                .build();

        String response = mockMvc.perform(post("/api/booking/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        JsonNode foundNode = null;
        for (JsonNode node : jsonNode) {
            if (node.get("roomName").asText().equals("DeluxeTest Room") &&
                    node.get("hotelName").asText().equals("Grand Hotel Hanoi")) {
                foundNode = node;
                break;
            }
        }
        assertThat(foundNode).isNotNull();
        assertThat(foundNode.get("price").asDouble()).isEqualTo(250000);
        System.out.println("Found room: " + foundNode);
    }

    // Kiểm tra đặt ngày đêm, nhận phòng sớm 1h so khung 14h chuẩn, không vượt số người lớn tiêu chuẩn, số trẻ em bằng số trẻ em miễn phí
    @Test
    public void testBooking_FullDay_EarlyCheckIn_OneHourBeforeStandardTime_WithinAdultLimit_EqualFreeChildren_ShouldIncludeEarlyCheckInFee() throws Exception {
        BookingSearchRequest searchRequest = BookingSearchRequest.builder()
                .infoSearch("DeluxeTest Room")
                .city("Hanoi")
                .district("Hoan Kiem")
                .checkInDate("2024-06-05")
                .checkOutDate("2024-06-06")
                .checkInTime("13:00")
                .checkOutTime("11:00")
                .adults(2)
                .children(1)
                .bedNumber(1)
                .priceFrom(0.0)
                .priceTo(600000.0)
                .sortBy("price_asc")
                .services(List.of())
                .build();

        String response = mockMvc.perform(post("/api/booking/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        JsonNode foundNode = null;
        for (JsonNode node : jsonNode) {
            if (node.get("roomName").asText().equals("DeluxeTest Room") &&
                    node.get("hotelName").asText().equals("Grand Hotel Hanoi")) {
                foundNode = node;
                break;
            }
        }
        assertThat(foundNode).isNotNull();
        assertThat(foundNode.get("price").asDouble()).isEqualTo(500000);
        System.out.println("Found room: " + foundNode);
    }

    // Kiểm tra đặt ngày đêm, trả phòng muộn 1h so khung 11h chuẩn, không vượt số người lớn tiêu chuẩn, số trẻ em bằng số trẻ em miễn phí
    @Test
    public void testBooking_FullDay_LateCheckOut_OneHourAfterStandardTime_WithinAdultLimit_EqualFreeChildren_ShouldIncludeLateCheckOutFee() throws Exception {
        BookingSearchRequest searchRequest = BookingSearchRequest.builder()
                .infoSearch("DeluxeTest Room")
                .city("Hanoi")
                .district("Hoan Kiem")
                .checkInDate("2024-06-05")
                .checkOutDate("2024-06-06")
                .checkInTime("14:00")
                .checkOutTime("12:00")
                .adults(2)
                .children(1)
                .bedNumber(1)
                .priceFrom(0.0)
                .priceTo(600000.0)
                .sortBy("price_asc")
                .services(List.of())
                .build();

        String response = mockMvc.perform(post("/api/booking/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        JsonNode foundNode = null;
        for (JsonNode node : jsonNode) {
            if (node.get("roomName").asText().equals("DeluxeTest Room") &&
                    node.get("hotelName").asText().equals("Grand Hotel Hanoi")) {
                foundNode = node;
                break;
            }
        }
        assertThat(foundNode).isNotNull();
        assertThat(foundNode.get("price").asDouble()).isEqualTo(500000);
        System.out.println("Found room: " + foundNode);
    }

    // Kiểm tra sắp xếp giá từ thấp đến cao
    @Test
    public void testSearchHotels_SortByPriceAscending_ShouldReturnSortedByPriceAsc() throws Exception {
        // Arrange
        BookingSearchRequest searchRequest = BookingSearchRequest.builder()
                .infoSearch("Room")
                .city("Hanoi")
                .district("Hoan Kiem")
                .checkInDate("2024-06-05")
                .checkOutDate("2024-06-06")
                .checkInTime("14:00")
                .checkOutTime("12:00")
                .adults(2)
                .children(1)
                .bedNumber(1)
                .priceFrom(0.0)
                .priceTo(1000000.0)
                .sortBy("price_asc")    // Sắp xếp theo giá tăng dần
                .services(List.of())
                .build();

        // Act
        String response = mockMvc.perform(post("/api/booking/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);

        // Assert
        double previousPrice = 0.0;
        for (JsonNode node : jsonNode) {
            double currentPrice = node.get("price").asDouble();
            assertThat(currentPrice).isGreaterThanOrEqualTo(previousPrice);
            previousPrice = currentPrice;
        }
    }

    // Kiểm tra sắp xếp giá từ cao đến thấp
    @Test
    public void testSearchHotels_SortByPriceDescending_ShouldReturnSortedByPriceDesc() throws Exception {
        // Arrange
        BookingSearchRequest searchRequest = BookingSearchRequest.builder()
                .infoSearch("Room")
                .city("Hanoi")
                .district("Hoan Kiem")
                .checkInDate("2024-06-05")
                .checkOutDate("2024-06-06")
                .checkInTime("14:00")
                .checkOutTime("12:00")
                .adults(2)
                .children(1)
                .bedNumber(1)
                .priceFrom(0.0)
                .priceTo(1000000.0)
                .sortBy("price_desc")    // Sắp xếp theo giá giảm dần
                .services(List.of())
                .build();

        // Act
        String response = mockMvc.perform(post("/api/booking/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);

        // Assert
        double previousPrice = Double.MAX_VALUE;
        for (JsonNode node : jsonNode) {
            double currentPrice = node.get("price").asDouble();
            assertThat(currentPrice).isLessThanOrEqualTo(previousPrice);
            previousPrice = currentPrice;
        }
    }

    // Kiểm tra sắp xếp theo đánh giá giảm dần
    @Test
    public void testSearchHotels_SortByRatingDescending_ShouldReturnSortedByRatingDesc() throws Exception {
        // Arrange
        BookingSearchRequest searchRequest = BookingSearchRequest.builder()
                .infoSearch("Room")
                .city("Hanoi")
                .district("Hoan Kiem")
                .checkInDate("2024-06-05")
                .checkOutDate("2024-06-06")
                .checkInTime("14:00")
                .checkOutTime("12:00")
                .adults(2)
                .children(1)
                .bedNumber(1)
                .priceFrom(0.0)
                .priceTo(1000000.0)
                .sortBy("rating_desc")    // Sắp xếp theo đánh giá giảm dần
                .services(List.of())
                .build();

        // Act
        String response = mockMvc.perform(post("/api/booking/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);

        // Assert
        double previousRating = Double.MAX_VALUE;
        for (JsonNode node : jsonNode) {
            double currentRating = node.get("rating").asDouble();
            assertThat(currentRating).isLessThanOrEqualTo(previousRating);
            previousRating = currentRating;
        }
    }

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
        assertThat(jsonNode.get("status").asText()).isEqualTo("PENDING");
    }

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
