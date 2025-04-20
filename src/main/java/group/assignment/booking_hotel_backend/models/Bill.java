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
public class Bill extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer billId;
    private Double totalPrice;
    private Boolean paidStatus;

//    @OneToMany(mappedBy="bill")
    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookingList;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}