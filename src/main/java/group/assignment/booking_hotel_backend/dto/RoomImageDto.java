package group.assignment.booking_hotel_backend.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
public class RoomImageDto {
    private Integer imgId;
    private String url;
}
