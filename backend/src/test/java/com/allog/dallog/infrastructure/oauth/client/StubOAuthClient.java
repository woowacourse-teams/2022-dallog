package com.allog.dallog.infrastructure.oauth.client;

import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.OAUTH_DISPLAY_NAME;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.OAUTH_EMAIL;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.OAUTH_PROFILE_IMAGE_URI;

import com.allog.dallog.auth.dto.OAuthMember;
import com.allog.dallog.auth.support.OAuthClient;

public class StubOAuthClient implements OAuthClient {

    @Override
    public OAuthMember getOAuthMember(final String code) {
        return new OAuthMember(OAUTH_EMAIL, OAUTH_DISPLAY_NAME, OAUTH_PROFILE_IMAGE_URI);
    }
}
