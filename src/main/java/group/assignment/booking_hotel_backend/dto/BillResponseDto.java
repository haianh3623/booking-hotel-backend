package group.assignment.booking_hotel_backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
public class BillResponseDto {
    private Integer billId;
    private Double totalPrice;
    private Boolean paidStatus;
    private Integer userId;
}
