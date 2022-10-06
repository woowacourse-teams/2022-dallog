package com.allog.dallog.domain.auth.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class InMemoryDallogTokenRepository implements TokenRepository {

    private static final Map<Long, String> TOKEN_REPOSITORY = new ConcurrentHashMap<>();

    @Override
    public String save(final Long memberId, final String refreshToken) {
        TOKEN_REPOSITORY.put(memberId, refreshToken);
        return TOKEN_REPOSITORY.get(memberId);
    }

    @Override
    public void deleteAll() {
        TOKEN_REPOSITORY.clear();
    }

    @Override
    public boolean exist(final Long memberId) {
        return TOKEN_REPOSITORY.containsKey(memberId);
    }

    @Override
    public String getToken(final Long memberId) {
        return TOKEN_REPOSITORY.get(memberId);
    }
}
