package group.assignment.booking_hotel_backend.security;//package group.assignment.booking_hotel_backend.security;
//
//public class AuthResponse {
//    private final String jwt;
//
//    public AuthResponse(String jwt) {
//        this.jwt = jwt;
//    }
//
//    public String getJwt() {
//        return jwt;
//    }
//}


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
}
