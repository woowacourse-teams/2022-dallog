package com.allog.dallog.auth.application;

import com.allog.dallog.auth.dto.OAuthMember;
import com.allog.dallog.auth.dto.response.OAuthAccessTokenResponse;

public interface OAuthClient {

    OAuthMember getOAuthMember(final String code, final String redirectUri);

    OAuthAccessTokenResponse getAccessToken(final String refreshToken);
}
