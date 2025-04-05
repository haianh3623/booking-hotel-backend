package group.assignment.booking_hotel_backend.models;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer imgId;
    private String url;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
}