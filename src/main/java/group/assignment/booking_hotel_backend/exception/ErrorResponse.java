package group.assignment.booking_hotel_backend.exception;
import  lombok.Data;
@Data
public class ErrorResponse {
    private String message;
    private String field;

    public ErrorResponse(String message, String field) {
        this.message = message;
        this.field = field;
    }
}
