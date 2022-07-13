package com.allog.dallog.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final String GOOGLE_OAUTH_END_POINT = "https://accounts.google.com/o/oauth2/v2/auth";

    private final String googleRedirectUri;
    private final String googleClientId;
    private final String googleClientSecret;

    public AuthService(@Value("${oauth.google.redirect_uri}") final String googleRedirectUri,
                       @Value("${oauth.google.client_id}") final String googleClientId,
                       @Value("${oauth.google.client_secret}") final String googleClientSecret) {
        this.googleRedirectUri = googleRedirectUri;
        this.googleClientId = googleClientId;
        this.googleClientSecret = googleClientSecret;
    }

    public String generateGoogleLink() {
        return GOOGLE_OAUTH_END_POINT + "?"
                + "client_id=" + googleClientId + "&"
                + "redirect_uri=" + googleRedirectUri + "&"
                + "response_type=code&"
                + "scope=https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email";
    }
}
