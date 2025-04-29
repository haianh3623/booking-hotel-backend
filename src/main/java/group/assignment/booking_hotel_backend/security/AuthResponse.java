package group.assignment.booking_hotel_backend.security;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String jwt;
    private Integer userId;
    private String[] roles;
}