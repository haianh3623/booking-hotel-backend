package group.assignment.booking_hotel_backend.models;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noti_id")
    private Integer notiId;
    private String content;
    @Column(name = "notification_time")
    private LocalDateTime notificationTime;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}