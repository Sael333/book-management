package bookmanagement.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class BookingResponse {
    private String bookingId;
    private String name;
    private int boxOffice;
    private LocalDateTime creationDate;
    private LocalDateTime endDate;
    private int securityCode;
    private int errorCode;
}
