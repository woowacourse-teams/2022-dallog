package com.allog.dallog.domain.auth.application;

public interface TokenProvider {

    String createToken(final String payload);

    String getPayload(final String token);

    void validateToken(final String token);
}
