package com.allog.dallog.domain.auth.domain;

public interface TokenRepository {

    void save(final Long memberId, final String refreshToken);

    void deleteAll();

    boolean exist(final Long memberId);

    String getToken(final Long memberId);
}
