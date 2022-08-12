package com.allog.dallog.infrastructure.oauth.uri;

import com.allog.dallog.domain.auth.application.OAuthUri;
import com.allog.dallog.global.config.properties.GoogleProperties;
import org.springframework.stereotype.Component;

@Component
public class GoogleOAuthUri implements OAuthUri {

    private final GoogleProperties properties;

    public GoogleOAuthUri(final GoogleProperties properties) {
        this.properties = properties;
    }

    @Override
    public String generate() {
        return properties.getOAuthEndPoint() + "?"
                + "client_id=" + properties.getClientId() + "&"
                + "redirect_uri=" + properties.getRedirectUri() + "&"
                + "response_type=code&"
                + "scope=" + String.join(" ", properties.getScopes()) + "&"
                + "access_type=" + properties.getAccessType() + "&"
                + "prompt=" + properties.getPrompt();
    }
}
