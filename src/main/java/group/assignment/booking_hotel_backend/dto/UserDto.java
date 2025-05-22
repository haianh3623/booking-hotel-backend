package group.assignment.booking_hotel_backend.dto;
import group.assignment.booking_hotel_backend.models.BaseEntity;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDto {
    private Integer userId;
    private String fullName;
    private String avatarUrl;
    private String phone;
    private String email;
    private String username;
    private String password;
    private Integer score;
}