package com.allog.dallog.global.config.cache;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.cache.concurrent.ConcurrentMapCache;

public class ExpiringConcurrentMapCache extends ConcurrentMapCache {

    private final Map<Object, LocalDateTime> expires = new ConcurrentHashMap<>();
    private final long expireAfter;

    public ExpiringConcurrentMapCache(final String name, final long expireAfter) {
        super(name);

        this.expireAfter = expireAfter;
    }

    @Override
    protected Object lookup(final Object key) {
        LocalDateTime expiredDate = expires.get(key);
        if (Objects.isNull(expiredDate) || isCacheValid(expiredDate)) {
            return super.lookup(key);
        }

        expires.remove(key);
        super.evict(key);
        return null;
    }

    @Override
    public void put(final Object key, final Object value) {
        LocalDateTime expiredAt = LocalDateTime.now().plusSeconds(expireAfter);
        expires.put(key, expiredAt);

        super.put(key, value);
    }

    public void evictAllExpired() {
        ConcurrentMap<Object, Object> nativeCache = getNativeCache();

        nativeCache.keySet()
                .stream()
                .filter(cacheKey -> !isCacheValid(expires.get(cacheKey)))
                .forEach(super::evict);
    }

    private boolean isCacheValid(final LocalDateTime expiredDate) {
        return LocalDateTime.now().isBefore(expiredDate);
    }
}
