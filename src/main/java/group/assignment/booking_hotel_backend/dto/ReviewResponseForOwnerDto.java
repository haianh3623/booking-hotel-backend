package group.assignment.booking_hotel_backend.dto;

public package group.assignment.booking_hotel_backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
public class ReviewResponseForOwnerDto {
    private Integer reviewId;
    private String content;
    private Integer rating;
    private String guestName;
    private String roomName;
    private LocalDateTime createdAt;
    private String ownerReply;
} {
    
}
