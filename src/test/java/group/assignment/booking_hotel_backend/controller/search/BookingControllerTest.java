package group.assignment.booking_hotel_backend.controller.search;
import group.assignment.booking_hotel_backend.config.AuditAwareImpl;
import group.assignment.booking_hotel_backend.controller.BookingController;
import group.assignment.booking_hotel_backend.dto.BookingSearchRequest;
import group.assignment.booking_hotel_backend.dto.BookingSearchResponse;
import group.assignment.booking_hotel_backend.repository.UserRepository; // Giả sử BookingController không trực tiếp dùng
import group.assignment.booking_hotel_backend.security.CustomUserDetailsService; // Giả sử BookingController không trực tiếp dùng
import group.assignment.booking_hotel_backend.security.JwtRequestFilter;
import group.assignment.booking_hotel_backend.security.JwtUtil; // Giả sử BookingController không trực tiếp dùng
import group.assignment.booking_hotel_backend.services.BookingService;
import group.assignment.booking_hotel_backend.services.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper; // Thêm ObjectMapper để chuyển đổi response thành JSON string
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.metamodel.Metamodel;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays; // Sử dụng Arrays.asList nếu cần
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is; // Hamcrest matchers for JSON path
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath; // JSON path assertions

@WebMvcTest(
        controllers = BookingController.class, // Chỉ định controller cần test
        excludeFilters = {
                // Loại bỏ các bean không cần thiết cho test controller này
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                        AuditAwareImpl.class,     // Bean AuditAware thực tế
                        JwtRequestFilter.class    // Filter JWT nếu không test security
                })
        },
        excludeAutoConfiguration = {
                // Loại bỏ các auto-configuration không cần thiết
                SecurityAutoConfiguration.class,
                JpaRepositoriesAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class,
                DataSourceAutoConfiguration.class
        }
)
// Cho phép override bean definition, cần thiết cho TestJpaMockConfiguration
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
public class BookingControllerTest {

    // Test Configuration để cung cấp các mock bean cho hạ tầng JPA Auditing
    @TestConfiguration
    static class TestJpaMockConfiguration {

        @Bean(name = "jpaMappingContext")
        @Primary
        public JpaMetamodelMappingContext jpaMetamodelMappingContext() {
            // System.out.println("Cung cấp JpaMetamodelMappingContext giả từ TestJpaMockConfiguration");
            Metamodel metamodel = Mockito.mock(Metamodel.class);
            Set<Metamodel> metamodelSet = new HashSet<>();
            metamodelSet.add(metamodel);
            return new JpaMetamodelMappingContext(metamodelSet);
        }

        @Bean(name = "jpaAuditingHandler")
        @Primary
        public AuditingHandler auditingHandler(JpaMetamodelMappingContext jpaMetamodelMappingContext) {
            // System.out.println("Cung cấp AuditingHandler giả từ TestJpaMockConfiguration");
            return Mockito.mock(AuditingHandler.class);
        }

        @Bean(name = "auditAwareImpl") // Tên bean khớp với auditorAwareRef trong @EnableJpaAuditing
        @Primary
        public AuditorAware<String> testAuditAware() {
            // System.out.println("Cung cấp AuditorAware giả (auditAwareImpl) từ TestJpaMockConfiguration");
            return () -> Optional.of("TEST_AUDITOR_MOCK");
        }

        // Các mock EntityManagerFactory và EntityManager này có thể cần nếu có gì đó
        // sâu trong Spring cố gắng tìm chúng, dù chúng ta đã exclude JPA auto-config.
        // Nếu test chạy mà không có chúng, bạn có thể xóa đi.
        @Bean
        @Primary
        public EntityManagerFactory entityManagerFactory() {
            EntityManagerFactory emf = Mockito.mock(EntityManagerFactory.class);
            Metamodel metamodel = Mockito.mock(Metamodel.class);
            when(emf.getMetamodel()).thenReturn(metamodel);
            return emf;
        }

        @Bean
        @Primary
        public jakarta.persistence.EntityManager entityManager(EntityManagerFactory emf) {
            return SharedEntityManagerCreator.createSharedEntityManager(emf);
        }
    }

    @Autowired
    private MockMvc mockMvc; // MockMvc để thực hiện HTTP request giả

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper để chuyển đổi object sang JSON và ngược lại

    // Mock các service mà BookingController phụ thuộc
    @MockitoBean
    private BookingService bookingService;

    @MockitoBean
    private RoomService roomService; // Ngay cả khi không dùng trực tiếp trong test này, nếu BookingController có inject thì cần mock

    // Các @MockitoBean này có thể không cần thiết nếu BookingController không trực tiếp inject chúng
    // và các component khác (như JwtRequestFilter) đã được exclude.
    // Hãy xem xét xóa nếu không gây lỗi.
    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;
    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private JwtUtil jwtUtil;


    @Test
    void testSearchAvailableRooms_Success_ReturnsRoomList() throws Exception {
        // 1. Chuẩn bị dữ liệu đầu vào (Request)
        BookingSearchRequest searchRequest = BookingSearchRequest.builder()
                .city("New York")
                .checkInDate("2025-07-01")
                .checkOutDate("2025-07-05")
                .adults(2)
                .children(0)
                .bedNumber(1)
                .build();

        // 2. Chuẩn bị dữ liệu mong đợi trả về từ service (Service Response)
        BookingSearchResponse room1 = BookingSearchResponse.builder()
                .roomId(1)
                .roomName("Luxury Suite")
                .price(250.0)
                .hotelName("Grand Hotel")
                .address("123 Main St, New York")
                .rating(4.5)
                .services(Arrays.asList("wifi", "pool"))
                .build();

        BookingSearchResponse room2 = BookingSearchResponse.builder()
                .roomId(2)
                .roomName("Deluxe Room")
                .price(180.0)
                .hotelName("Plaza Hotel")
                .address("456 Park Ave, New York")
                .rating(4.2)
                .services(Arrays.asList("wifi", "gym"))
                .build();

        List<BookingSearchResponse> expectedServiceResponse = Arrays.asList(room1, room2);


        expectedServiceResponse.forEach(room -> System.out.println(room));

        // 3. Mock hành vi của BookingService
        // Khi bookingService.searchAvailableRooms được gọi với bất kỳ BookingSearchRequest nào,
        // nó sẽ trả về danh sách expectedServiceResponse đã chuẩn bị.
        when(bookingService.searchAvailableRooms(any(BookingSearchRequest.class)))
                .thenReturn(expectedServiceResponse);

        // 4. Thực hiện request POST đến controller và kiểm tra kết quả
        ResultActions resultActions = mockMvc.perform(post("/api/booking/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchRequest))); // Chuyển request object thành JSON

        String responseContent = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("Response from controller: " + responseContent);

        // 5. Kiểm tra HTTP Status và nội dung JSON của response
        resultActions
                .andExpect(status().isOk()) // Mong đợi HTTP 200 OK
                // Kiểm tra từng trường của từng object trong mảng JSON response
                .andExpect(jsonPath("$.length()", is(expectedServiceResponse.size()))) // Kiểm tra số lượng phần tử trong mảng
                .andExpect(jsonPath("$[0].roomId", is(room1.getRoomId())))
                .andExpect(jsonPath("$[0].roomName", is(room1.getRoomName())))
                .andExpect(jsonPath("$[0].price", is(room1.getPrice())))
                .andExpect(jsonPath("$[0].hotelName", is(room1.getHotelName())))
                .andExpect(jsonPath("$[0].address", is(room1.getAddress())))
                .andExpect(jsonPath("$[0].rating", is(room1.getRating())))
                .andExpect(jsonPath("$[0].services[0]", is("wifi")))
                .andExpect(jsonPath("$[0].services[1]", is("pool")))
                .andExpect(jsonPath("$[1].roomId", is(room2.getRoomId())))
                .andExpect(jsonPath("$[1].roomName", is(room2.getRoomName())))
                .andExpect(jsonPath("$[1].price", is(room2.getPrice())));
        // ... thêm các kiểm tra khác cho room2 nếu cần

        // 6. (Tùy chọn) Xác minh rằng phương thức của service đã được gọi đúng 1 lần
        verify(bookingService, times(1)).searchAvailableRooms(any(BookingSearchRequest.class));
    }

    @Test
    void testSearchAvailableRooms_ServiceThrowsException_ReturnsInternalServerError() throws Exception {
        // 1. Chuẩn bị dữ liệu đầu vào (Request)
        BookingSearchRequest searchRequest = BookingSearchRequest.builder()
                .city("Error City")
                .checkInDate("2025-08-01")
                .checkOutDate("2025-08-05")
                .adults(1)
                .build();

        // 2. Mock hành vi của BookingService để nó ném ra một exception
        String errorMessage = "Database connection failed";
        when(bookingService.searchAvailableRooms(any(BookingSearchRequest.class)))
                .thenThrow(new RuntimeException(errorMessage));

        // 3. Thực hiện request POST và kiểm tra kết quả
        ResultActions resultActions = mockMvc.perform(post("/api/booking/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchRequest)));

        // 4. Kiểm tra HTTP Status
        resultActions
                .andExpect(status().isInternalServerError()); // Mong đợi HTTP 500 Internal Server Error
        // Controller của bạn trả về body null khi có lỗi, nên không cần kiểm tra content().
        // Nếu bạn muốn trả về một DTO lỗi, bạn có thể kiểm tra nó ở đây.

        // 5. Xác minh rằng phương thức của service đã được gọi
        verify(bookingService, times(1)).searchAvailableRooms(any(BookingSearchRequest.class));
    }

    @Test
    void testSearchAvailableRooms_EmptyResult_ReturnsOkWithEmptyList() throws Exception {
        // 1. Chuẩn bị dữ liệu đầu vào
        BookingSearchRequest searchRequest = BookingSearchRequest.builder()
                .city("No Rooms City")
                .checkInDate("2025-09-01")
                .checkOutDate("2025-09-05")
                .adults(1)
                .build();

        // 2. Mock service trả về danh sách rỗng
        when(bookingService.searchAvailableRooms(any(BookingSearchRequest.class)))
                .thenReturn(List.of()); // Trả về danh sách rỗng

        // 3. Thực hiện request
        ResultActions resultActions = mockMvc.perform(post("/api/booking/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchRequest)));

        // 4. Kiểm tra kết quả
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(0))); // Mong đợi mảng JSON rỗng

        // 5. Xác minh service được gọi
        verify(bookingService, times(1)).searchAvailableRooms(any(BookingSearchRequest.class));
    }
}