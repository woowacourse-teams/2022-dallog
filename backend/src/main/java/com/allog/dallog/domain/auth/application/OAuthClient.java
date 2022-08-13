package com.allog.dallog.domain.auth.application;

import com.allog.dallog.domain.auth.dto.OAuthMember;
import com.allog.dallog.domain.auth.dto.response.OAuthAccessTokenResponse;

public interface OAuthClient {

    OAuthMember getOAuthMember(final String code);

    OAuthAccessTokenResponse getAccessToken(final String refreshToken);
}
