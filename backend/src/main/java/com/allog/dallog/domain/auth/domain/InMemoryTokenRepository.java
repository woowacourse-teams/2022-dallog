package com.allog.dallog.domain.auth.domain;

import java.util.HashMap;
import java.util.Map;

public class InMemoryTokenRepository {

    private final static Map<Long, String> TokenRepository = new HashMap<>();

    public static void save(final Long memberId, final String refreshToken) {
        TokenRepository.put(memberId, refreshToken);
    }

    public static boolean exist(final Long memberId) {
        return TokenRepository.containsKey(memberId);
    }

    public static String getToken(final Long memberId) {
        return TokenRepository.get(memberId);
    }

    public static void deleteAll() {
        TokenRepository.clear();
    }
}
