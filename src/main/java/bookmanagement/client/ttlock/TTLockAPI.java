package bookmanagement.client.ttlock;

import bookmanagement.dto.KeyboardData;
import bookmanagement.dto.TokenTtlockResponse;
import bookmanagement.dto.UnlockDataResponse;
import bookmanagement.model.request.BookingRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@Component
@RequiredArgsConstructor
public class TTLockAPI {
    private final TTLockAPIConfig ttLockAPIConfig; // URL base de la API

    @Cacheable("ttlock-token")
    public String getAccessToken() throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        // Crear cuerpo de la solicitud para obtener el token
        Map<String, String> data = new HashMap<>();
        data.put("clientId", ttLockAPIConfig.getClientId());
        data.put("clientSecret", ttLockAPIConfig.getSecretId());
        data.put("username", ttLockAPIConfig.getUser());
        data.put("password", ttLockAPIConfig.getPass());
        String requestBody = encodeParams(data);


        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(ttLockAPIConfig.getApiUrl().concat(ttLockAPIConfig.getTokenPath())))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();

        if (response.statusCode() == 200) {
            TokenTtlockResponse tokenTtlockResponse = objectMapper.readValue(response.body(), TokenTtlockResponse.class);
            return  tokenTtlockResponse.getAccessToken();
        } else {
            throw new Exception("Error al autenticar: " + response.body());
        }
    }
    public KeyboardData getSecurityCodeCustomByLockId(String lockId, String accessToken, BookingRequest bookingRequest, String expiration, String passCode) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        // Crear cuerpo de la solicitud para obtener el token
        Map<String, String> body = new HashMap<>();

        body.put("clientId", ttLockAPIConfig.getClientId());
        body.put("accessToken", accessToken);
        body.put("lockId", lockId);
        body.put("keyboardPwd", passCode);
        body.put("keyboardPwdName", bookingRequest.getName());
        body.put("keyboardPwdType", "3");
        body.put("addType", "2");
        body.put("startDate", String.valueOf(ZonedDateTime.now(ZoneOffset.UTC).toInstant().toEpochMilli()));
        body.put("endDate", expiration);
        body.put("date", String.valueOf(System.currentTimeMillis()));
        String requestBody = encodeParams(body);


        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(ttLockAPIConfig.getApiUrl().concat(ttLockAPIConfig.getUnlockCustomPath())))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        if (response.statusCode() == 200) {
            KeyboardData unlockDataResponse = objectMapper.readValue(response.body(), KeyboardData.class);
            return unlockDataResponse;
        } else {
            throw new Exception("Error al autenticar: " + response.body());
        }
    }

    public UnlockDataResponse getSecurityCodeByLockId(String lockId, String accessToken, BookingRequest bookingRequest) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        // Crear cuerpo de la solicitud para obtener el token
        Map<String, String> queryParams = new HashMap<>();

        queryParams.put("clientId", ttLockAPIConfig.getClientId());
        queryParams.put("accessToken", accessToken);
        queryParams.put("lockId", lockId);
        queryParams.put("keyboardPwdType", "4");
        queryParams.put("startDate", String.valueOf(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()));
        queryParams.put("endDate", String.valueOf(bookingRequest.getEndDate().toInstant(ZoneOffset.UTC).toEpochMilli()));
        queryParams.put("date", String.valueOf(System.currentTimeMillis()));
        String requestBody = encodeParams(queryParams);


        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(ttLockAPIConfig.getApiUrl().concat(ttLockAPIConfig.getUnlockCustomPath())))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        if (response.statusCode() == 200) {
            UnlockDataResponse unlockDataResponse = objectMapper.readValue(response.body(), UnlockDataResponse.class);
            return unlockDataResponse;
        } else {
            throw new Exception("Error al autenticar: " + response.body());
        }
    }

    public static String encodeParams(Map<String, String> params) {
        StringBuilder encodedParams = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (encodedParams.length() > 0) {
                encodedParams.append("&");
            }

            String encodedKey = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8);
            String encodedValue = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8);

            encodedParams.append(encodedKey)
                    .append("=")
                    .append(encodedValue);
        }

        return encodedParams.toString();
    }

    private static String buildUrlWithParams(String baseUrl, Map<String, String> queryParams) {
        StringJoiner sj = new StringJoiner("&", baseUrl + "?", "");
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            sj.add(entry.getKey() + "=" + entry.getValue());
        }
        return sj.toString();
    }
}
