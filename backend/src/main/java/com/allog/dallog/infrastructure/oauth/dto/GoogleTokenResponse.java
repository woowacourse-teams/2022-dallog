package com.allog.dallog.infrastructure.oauth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleTokenResponse {

    private String refreshToken;
    private String idToken;

    private GoogleTokenResponse() {
    }

    public GoogleTokenResponse(final String refreshToken, final String idToken) {
        this.refreshToken = refreshToken;
        this.idToken = idToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getIdToken() {
        return idToken;
    }
}
