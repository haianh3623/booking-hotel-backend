package group.assignment.booking_hotel_backend.models;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;
    private String name;
    private LocalDateTime timeCreated;
    @ManyToMany
    @JoinTable(
            name="user_role",
            joinColumns=@JoinColumn(name="role_id"),
            inverseJoinColumns=@JoinColumn(name="user_id")
    )
    private List<User> users;

    public Role(String name) {
        this.name = name;
    }
}
