
package group.assignment.booking_hotel_backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
public class DeviceTokenRequest {
    private Integer userId;
    private String deviceToken;
    private String deviceType; // "android" or "ios"
}