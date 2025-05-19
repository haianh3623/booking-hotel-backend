package group.assignment.booking_hotel_backend.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
public class RegistrationRequest {
    private String fullName;
    private String phone;
    private String email;
    private String username;
    private String password;
}

