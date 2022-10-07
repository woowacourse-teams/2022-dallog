package com.allog.dallog.domain.auth.application;

import com.allog.dallog.domain.auth.domain.AuthToken;

public interface TokenCreator {

    AuthToken createDallogToken(final Long memberId);

    AuthToken renewDallogToken(final String outRefreshToken);

    Long extractPayload(final String accessToken);
}
