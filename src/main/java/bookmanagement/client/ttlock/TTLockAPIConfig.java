package bookmanagement.client.ttlock;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class TTLockAPIConfig {
    @Value("${application.ttlock.url}")
    private String apiUrl;
    @Value("${application.ttlock.tokenPath}")
    private String tokenPath;
    @Value("${application.ttlock.unlockPath}")
    private String unlockPath;
    @Value("${application.ttlock.unlockCustomPath}")
    private String unlockCustomPath;
    @Value("${application.ttlock.clientid}")
    private String clientId;
    @Value("${application.ttlock.secretid}")
    private String secretId;
    @Value("${application.ttlock.user}")
    private String user;
    @Value("${application.ttlock.pass}")
    private String pass;

}
