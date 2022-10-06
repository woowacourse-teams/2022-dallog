package com.allog.dallog.domain.auth.application;

import com.allog.dallog.domain.auth.domain.DallogToken;

public interface TokenManager {

    DallogToken createDallogToken(final Long memberId);

    DallogToken renewDallogToken(final String outRefreshToken);

    Long extractPayload(final String accessToken);
}
