package group.assignment.booking_hotel_backend.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingServiceResponse {
    private Integer serviceId;
    private String serviceName;
}
