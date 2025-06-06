package group.assignment.booking_hotel_backend.models;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Service extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer serviceId;
    private String serviceName;
    private Double price;

    @ManyToMany
    @JoinTable(
            name="room_service",
            joinColumns=@JoinColumn(name="service_id"),
            inverseJoinColumns=@JoinColumn(name="room_id")
    )
    private List<Room> roomList;
}