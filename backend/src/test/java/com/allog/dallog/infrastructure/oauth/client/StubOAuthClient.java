package com.allog.dallog.infrastructure.oauth.client;

import com.allog.dallog.auth.dto.OAuthMember;
import com.allog.dallog.auth.support.OAuthClient;

public class StubOAuthClient implements OAuthClient {

    @Override
    public OAuthMember getOAuthMember(final String code) {
        return new OAuthMember("dev.hyeonic@gmail.com", "Fake Name", "Fake Profile Image Url");
    }
}
