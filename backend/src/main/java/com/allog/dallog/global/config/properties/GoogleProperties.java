package com.allog.dallog.global.config.properties;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties("oauth.google")
@ConstructorBinding
public class GoogleProperties {

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String oAuthEndPoint;
    private final String responseType;
    private final List<String> scopes;
    private final String tokenUri;
    private final String grantType;
    private final String accessType;
    private final String prompt;

    public GoogleProperties(final String clientId, final String clientSecret, final String redirectUri,
                            final String oAuthEndPoint, final String responseType, final List<String> scopes,
                            final String tokenUri, final String grantType, final String accessType,
                            final String prompt) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.oAuthEndPoint = oAuthEndPoint;
        this.responseType = responseType;
        this.scopes = scopes;
        this.tokenUri = tokenUri;
        this.grantType = grantType;
        this.accessType = accessType;
        this.prompt = prompt;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getOAuthEndPoint() {
        return oAuthEndPoint;
    }

    public String getResponseType() {
        return responseType;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public String getTokenUri() {
        return tokenUri;
    }

    public String getGrantType() {
        return grantType;
    }

    public String getAccessType() {
        return accessType;
    }

    public String getPrompt() {
        return prompt;
    }
}
