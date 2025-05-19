package group.assignment.booking_hotel_backend.dto;

import lombok.*;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
public class UserResponseDto {
    private Integer userId;
    private String fullName;
    private String phone;
    private String email;
    private String username;
    private Integer score;
    private List<RoleDto> roleDtoList;
}
