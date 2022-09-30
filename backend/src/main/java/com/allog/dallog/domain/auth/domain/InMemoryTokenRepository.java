package com.allog.dallog.domain.auth.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTokenRepository {

    private static final Map<Long, String> TOKEN_REPOSITORY = new ConcurrentHashMap<>();

    public static void save(final Long memberId, final String refreshToken) {
        TOKEN_REPOSITORY.put(memberId, refreshToken);
    }

    public static boolean exist(final Long memberId) {
        return TOKEN_REPOSITORY.containsKey(memberId);
    }

    public static String getToken(final Long memberId) {
        return TOKEN_REPOSITORY.get(memberId);
    }

    public static void deleteAll() {
        TOKEN_REPOSITORY.clear();
    }
}
