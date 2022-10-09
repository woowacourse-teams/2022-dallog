package com.allog.dallog.domain.auth.application;

import com.allog.dallog.domain.auth.domain.AuthToken;

public interface TokenCreator {

    AuthToken createAuthToken(final Long memberId);

    AuthToken renewAuthToken(final String outRefreshToken);

    Long extractPayload(final String accessToken);
}
