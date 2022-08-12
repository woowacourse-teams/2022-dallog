package com.allog.dallog.infrastructure.oauth.client;

import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_MEMBER_인증_코드;
import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_OAUTH_ACCESS_TOKEN;
import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_OAUTH_CREATOR;
import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_OAUTH_EXPIRES_IN;
import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_OAUTH_MEMBER;
import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_OAUTH_SCOPE;
import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_OAUTH_TOKEN_TYPE;

import com.allog.dallog.domain.auth.application.OAuthClient;
import com.allog.dallog.domain.auth.dto.OAuthMember;
import com.allog.dallog.domain.auth.dto.response.OAuthAccessTokenResponse;

public class StubOAuthClient implements OAuthClient {

    @Override
    public OAuthMember getOAuthMember(final String code) {
        if (code.equals(STUB_MEMBER_인증_코드)) {
            return STUB_OAUTH_MEMBER();
        }
        return STUB_OAUTH_CREATOR();
    }

    @Override
    public OAuthAccessTokenResponse geAccessToken(final String refreshToken) {
        return new OAuthAccessTokenResponse(STUB_OAUTH_ACCESS_TOKEN, STUB_OAUTH_EXPIRES_IN, STUB_OAUTH_SCOPE,
                STUB_OAUTH_TOKEN_TYPE);
    }
}
