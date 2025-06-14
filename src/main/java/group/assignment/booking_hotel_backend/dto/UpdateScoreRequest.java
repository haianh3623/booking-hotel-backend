package group.assignment.booking_hotel_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateScoreRequest {
    private Integer userId;
    private Integer score;
}
