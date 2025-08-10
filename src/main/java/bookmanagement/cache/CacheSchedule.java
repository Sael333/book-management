package bookmanagement.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheSchedule {
    private final RefreshTokenCache bookManagementCache;

    @Scheduled(fixedRateString = "${application.cache.refresh}")
    private void refreshTtlockApiToken() throws Exception {
        bookManagementCache.refreshCache();
    }
}
