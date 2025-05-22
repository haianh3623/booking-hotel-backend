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
public class Room extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roomId;
    private String roomName;
    private Double area;
    private Double comboPrice2h;
    private Double pricePerNight;
    private Double extraHourPrice;
    private Integer standardOccupancy;
    private Integer maxOccupancy;
    private Integer numChildrenFree;
    private String roomImg;
    private Integer bedNumber;
    private Double extraAdult;
    private String description;
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomImage> roomImageList;

    @OneToMany(mappedBy="room")
    private List<Booking> bookingList;


    @ManyToMany
    @JoinTable(
            name="room_service",
            joinColumns=@JoinColumn(name="room_id"),
            inverseJoinColumns=@JoinColumn(name="service_id")
    )
    private List<Service> serviceList;
}