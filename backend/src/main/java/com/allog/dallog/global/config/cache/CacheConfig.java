package com.allog.dallog.global.config.cache;

import java.util.List;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfig {

    public static final String GOOGLE_CALENDAR = "googleCalendar";
    private static final long EXPIRE_AFTER = 60 * 60 * 3;

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(List.of(new ExpiringConcurrentMapCache(GOOGLE_CALENDAR, EXPIRE_AFTER)));

        return simpleCacheManager;
    }

    @Scheduled(cron = "0 0 0 * * *")
    private void evict() {
        ExpiringConcurrentMapCache cache = (ExpiringConcurrentMapCache) cacheManager().getCache(GOOGLE_CALENDAR);
        cache.evictAllExpired();
    }
}
