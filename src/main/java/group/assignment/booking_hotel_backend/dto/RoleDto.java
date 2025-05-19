package group.assignment.booking_hotel_backend.dto;
import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RoleDto {
    private Integer roleId;
    private String name;
}
