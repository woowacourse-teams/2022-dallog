package com.allog.dallog.infrastructure.oauth.client;

import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_MEMBER_인증_코드;
import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_OAUTH_CREATOR;
import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_OAUTH_MEMBER;

import com.allog.dallog.domain.auth.application.OAuthClient;
import com.allog.dallog.domain.auth.dto.OAuthMember;

public class StubOAuthClient implements OAuthClient {

    @Override
    public OAuthMember getOAuthMember(final String code) {
        if (code.equals(STUB_MEMBER_인증_코드)) {
            return STUB_OAUTH_MEMBER();
        }
        return STUB_OAUTH_CREATOR();
    }
}
