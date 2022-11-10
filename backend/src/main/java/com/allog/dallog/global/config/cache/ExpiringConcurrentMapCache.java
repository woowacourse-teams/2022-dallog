package com.allog.dallog.global.config.cache;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.cache.concurrent.ConcurrentMapCache;

public class ExpiringConcurrentMapCache extends ConcurrentMapCache {

    private final Map<Object, LocalDateTime> expires = new HashMap<>();
    private final long expireAfter;

    public ExpiringConcurrentMapCache(final String name, final long expireAfter) {
        super(name);

        this.expireAfter = expireAfter;
    }

    @Override
    protected Object lookup(final Object key) {
        LocalDateTime expiredDate = expires.get(key);
        if (Objects.isNull(expiredDate) || LocalDateTime.now().isBefore(expiredDate)) {
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
}
