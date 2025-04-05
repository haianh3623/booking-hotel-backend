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

    @OneToMany(mappedBy="bill")
    private List<Booking> bookingList;
}