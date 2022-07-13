package com.allog.dallog.auth.service;

import com.allog.dallog.auth.dto.TokenResponse;
import com.allog.dallog.infrastructure.oauth.client.OAuthClient;
import com.allog.dallog.infrastructure.oauth.dto.OAuthMember;
import com.allog.dallog.infrastructure.oauth.endpoint.OAuthEndpoint;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final OAuthEndpoint oAuthEndpoint;
    private final OAuthClient oAuthClient;

    public AuthService(final OAuthEndpoint oAuthEndpoint, final OAuthClient oAuthClient) {
        this.oAuthEndpoint = oAuthEndpoint;
        this.oAuthClient = oAuthClient;
    }

    public String generateGoogleLink() {
        return oAuthEndpoint.generate();
    }

    public TokenResponse generateTokenWithCode(final String code) {
        OAuthMember oAuthMember = oAuthClient.getOAuthMember(code);
        return null;
    }
}
