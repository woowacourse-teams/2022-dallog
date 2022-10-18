package com.allog.dallog.infrastructure.oauth.uri;

import com.allog.dallog.domain.auth.application.OAuthUri;
import com.allog.dallog.global.config.properties.GoogleProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"dev", "local"})
public class DevGoogleOAuthUri implements OAuthUri {

    private final GoogleProperties properties;

    public DevGoogleOAuthUri(final GoogleProperties properties) {
        this.properties = properties;
    }

    @Override
    public String generate(final String redirectUri) {
        return properties.getOAuthEndPoint() + "?"
                + "client_id=" + properties.getClientId() + "&"
                + "redirect_uri=" + redirectUri + "&"
                + "response_type=code&"
                + "scope=" + String.join(" ", properties.getScopes()) + "&"
                + "access_type=" + properties.getAccessType() + "&"
                + "prompt=consent";
    }
}
