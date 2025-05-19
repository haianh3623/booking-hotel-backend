package group.assignment.booking_hotel_backend.dto;
import java.time.LocalDateTime;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDto {
    private Integer notiId;
    private String content;
    private LocalDateTime notificationTime;
    private Integer userId;
    private LocalDateTime createdAt;

    public NotificationDto(Integer notiId, String content, LocalDateTime notificationTime, Integer userId) {
        this.notiId = notiId;
        this.content = content;
        this.notificationTime = notificationTime;
        this.userId = userId;
    }
}
