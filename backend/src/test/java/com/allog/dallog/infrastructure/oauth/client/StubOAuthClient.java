package com.allog.dallog.infrastructure.oauth.client;

import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.DISPLAY_NAME;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.EMAIL;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.PROFILE_IMAGE_URI;

import com.allog.dallog.auth.dto.OAuthMember;
import com.allog.dallog.auth.support.OAuthClient;

public class StubOAuthClient implements OAuthClient {

    @Override
    public OAuthMember getOAuthMember(final String code) {
        return new OAuthMember(EMAIL, DISPLAY_NAME, PROFILE_IMAGE_URI);
    }
}
