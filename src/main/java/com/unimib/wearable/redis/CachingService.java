package com.unimib.wearable.redis;

import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Optional;

@AllArgsConstructor
public class CachingService {

    private final CacheManager cacheManager;

    @Scheduled(cron = "${time-to-live}")
    public void clearAllCaches() {
        cacheManager.getCacheNames()
                .forEach(cacheName -> Optional.ofNullable(cacheManager.getCache(cacheName)).ifPresent(Cache::clear));
    }
}
