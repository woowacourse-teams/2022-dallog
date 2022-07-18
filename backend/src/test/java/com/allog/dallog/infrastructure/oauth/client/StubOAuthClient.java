package com.allog.dallog.infrastructure.oauth.client;

import static com.allog.dallog.fixture.OAuthMemberFixtures.OAUTH_MEMBER;

import com.allog.dallog.auth.dto.OAuthMember;
import com.allog.dallog.auth.support.OAuthClient;

public class StubOAuthClient implements OAuthClient {

    @Override
    public OAuthMember getOAuthMember(final String code) {
        return OAUTH_MEMBER;
    }
}
