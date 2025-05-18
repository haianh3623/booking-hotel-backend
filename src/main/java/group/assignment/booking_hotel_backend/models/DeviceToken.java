package group.assignment.booking_hotel_backend.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "device_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceToken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "device_token", nullable = false, columnDefinition = "TEXT")
    private String deviceToken;
    
    @Column(name = "device_type", length = 50)
    private String deviceType; // "android", "ios"
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}