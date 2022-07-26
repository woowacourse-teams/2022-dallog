package com.allog.dallog.infrastructure.oauth.uri;

import com.allog.dallog.domain.auth.application.OAuthUri;
import com.allog.dallog.global.config.properties.GoogleProperties;
import org.springframework.stereotype.Component;

@Component
public class GoogleOAuthUri implements OAuthUri {

    private final GoogleProperties googleProperties;

    public GoogleOAuthUri(final GoogleProperties googleProperties) {
        this.googleProperties = googleProperties;
    }

    @Override
    public String generate() {
        return googleProperties.getoAuthEndPoint() + "?"
                + "client_id=" + googleProperties.getClientId() + "&"
                + "redirect_uri=" + googleProperties.getRedirectUri() + "&"
                + "response_type=code&"
                + "scope=" + String.join(" ", googleProperties.getScopes());
    }
}
