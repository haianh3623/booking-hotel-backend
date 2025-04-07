package group.assignment.booking_hotel_backend.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addressId;

    private String city;
    private String district;
    private String ward;
    private String specificAddress;

    @OneToOne(mappedBy = "address")
    private Hotel hotel;
}
