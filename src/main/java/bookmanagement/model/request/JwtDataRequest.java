package bookmanagement.model.request;

import lombok.Data;

@Data
public class JwtDataRequest {
    private String userId;
    private boolean paymentConfirm;
    private Long amount;
}
