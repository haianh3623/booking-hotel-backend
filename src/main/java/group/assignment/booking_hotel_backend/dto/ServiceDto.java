package group.assignment.booking_hotel_backend.dto;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
public class ServiceDto {
    private Integer serviceId;
    private String serviceName;
    private Double price;
}
