package com.allog.dallog.infrastructure.oauth.endpoint;

import com.allog.dallog.auth.support.OAuthEndpoint;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleOAuthEndpoint implements OAuthEndpoint {

    private static final String GOOGLE_OAUTH_END_POINT = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final List<String> SCOPES = List.of("https://www.googleapis.com/auth/userinfo.profile",
            "https://www.googleapis.com/auth/userinfo.email");

    private final String googleRedirectUri;
    private final String googleClientId;

    public GoogleOAuthEndpoint(@Value("${oauth.google.redirect_uri}") final String googleRedirectUri,
                               @Value("${oauth.google.client_id}") final String googleClientId) {
        this.googleRedirectUri = googleRedirectUri;
        this.googleClientId = googleClientId;
    }

    @Override
    public String generate() {
        return GOOGLE_OAUTH_END_POINT + "?"
                + "client_id=" + googleClientId + "&"
                + "redirect_uri=" + googleRedirectUri + "&"
                + "response_type=code&"
                + "scope=" + String.join(" ", SCOPES);
    }
}
