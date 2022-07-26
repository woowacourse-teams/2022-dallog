package com.allog.dallog.infrastructure.oauth.uri;

import com.allog.dallog.auth.support.OAuthUri;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleOAuthUri implements OAuthUri {

    private final String oauthEndPoint;
    private final String clientId;
    private final String redirectUri;
    private final List<String> scopes;

    public GoogleOAuthUri(@Value("${oauth.google.oauth-end-point}") final String oauthEndPoint,
                          @Value("${oauth.google.client-id}") final String clientId,
                          @Value("${oauth.google.redirect-uri}") final String redirectUri,
                          @Value("${oauth.google.scopes}") final List<String> scopes) {
        this.oauthEndPoint = oauthEndPoint;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.scopes = scopes;
    }

    @Override
    public String generate() {
        return oauthEndPoint + "?"
                + "client_id=" + clientId + "&"
                + "redirect_uri=" + redirectUri + "&"
                + "response_type=code&"
                + "scope=" + String.join(" ", scopes);
    }
}
