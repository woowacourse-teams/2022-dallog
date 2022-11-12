package com.allog.dallog.global.config.cache;

import java.util.List;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    public static final String GOOGLE_CALENDAR = "googleCalendar";
    private static final long EXPIRE_AFTER = 60 * 60 * 3;

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(List.of(new ExpiringConcurrentMapCache(GOOGLE_CALENDAR, EXPIRE_AFTER)));

        return simpleCacheManager;
    }
}
