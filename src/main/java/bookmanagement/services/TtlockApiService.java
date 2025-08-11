package bookmanagement.services;

import bookmanagement.client.ttlock.TTLockAPI;
import bookmanagement.dto.KeyboardData;
import bookmanagement.model.request.BookingRequest;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TtlockApiService {

    private final TTLockAPI ttLockAPI;

    public String generateAccessToken() {
        try {
            return ttLockAPI.getAccessToken();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void generateSecurityCode(String lockId, BookingRequest bookingRequest, String expiration, String passCode) {
        KeyboardData keyboardData;
        try {
            keyboardData = ttLockAPI.getSecurityCodeCustomByLockId(lockId, generateAccessToken(), bookingRequest, expiration, passCode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (StringUtils.isBlank(String.valueOf(keyboardData.getKeyboardPwdId()))) {
            log.error("Error while generating passcode");
        }
    }
}
