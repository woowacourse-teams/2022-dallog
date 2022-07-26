package com.allog.dallog.infrastructure.oauth.client;

import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_이름;
import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_이메일;
import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_프로필;

import com.allog.dallog.domain.auth.application.OAuthClient;
import com.allog.dallog.domain.auth.dto.OAuthMember;

public class StubOAuthClient implements OAuthClient {

    @Override
    public OAuthMember getOAuthMember(final String code) {
        return new OAuthMember(STUB_이메일, STUB_이름, STUB_프로필);
    }
}
