package com.allog.dallog.infrastructure.oauth.client;

import com.allog.dallog.infrastructure.oauth.dto.OAuthMember;
import org.springframework.stereotype.Component;

@Component
public class GoogleOAuthClient implements OAuthClient {

    @Override
    public OAuthMember getOAuthMember(final String code) {
        return null;
    }
}
