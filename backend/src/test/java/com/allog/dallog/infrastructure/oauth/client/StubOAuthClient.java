package com.allog.dallog.infrastructure.oauth.client;

import com.allog.dallog.infrastructure.oauth.dto.OAuthMember;

public class StubOAuthClient implements OAuthClient {

    @Override
    public OAuthMember getOAuthMember(final String code) {
        return new OAuthMember("Fake Email", "Fake Name", "Fake Profile Image Url");
    }
}
