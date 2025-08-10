package bookmanagement.cache;

import bookmanagement.client.ttlock.TTLockAPI;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenCache {

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private TTLockAPI ttLockAPI;

    @PostConstruct
    private void initCache() throws Exception {
        ttLockAPI.getAccessToken();
    }

    public void refreshCache() throws Exception {
        cacheManager.getCache("ttlock-token").clear();
        initCache();
    }
}
