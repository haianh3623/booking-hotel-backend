package group.assignment.booking_hotel_backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {
    private String fullName;
    private String avatarUrl;
    private String email;
    private String phone;
}
