package com.allog.dallog.infrastructure.oauth.uri;

import com.allog.dallog.domain.auth.application.OAuthUri;

public class StubOAuthUri implements OAuthUri {

    @Override
    public String generate(final String redirectUri) {
        return "https://localhost:3000";
    }
}
