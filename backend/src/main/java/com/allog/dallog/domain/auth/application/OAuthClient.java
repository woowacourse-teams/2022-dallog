package com.allog.dallog.domain.auth.application;

import com.allog.dallog.domain.auth.dto.OAuthAccessTokenResponse;
import com.allog.dallog.domain.auth.dto.OAuthMember;

public interface OAuthClient {

    OAuthMember getOAuthMember(final String code);

    OAuthAccessTokenResponse geAccessToken(final String refreshToken);
}
