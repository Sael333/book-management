package bookmanagement.model.response;

import lombok.Data;

import java.util.List;

@Data
public class AvailabilityResponse {
    private boolean available;
    private List<String> sizes;
}
