package com.allog.dallog.domain.auth.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OAuthAccessTokenResponse {

    private String accessToken;
    private String expiresIn;
    private String scope;
    private String tokenType;

    private OAuthAccessTokenResponse() {
    }

    public OAuthAccessTokenResponse(final String accessToken, final String expiresIn, final String scope,
                                    final String tokenType) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.scope = scope;
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public String getScope() {
        return scope;
    }

    public String getTokenType() {
        return tokenType;
    }
}
