package com.allog.dallog.domain.auth.dto;

public class TokenResponse {

    private String accessToken;

    private TokenResponse() {
    }

    public TokenResponse(final String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
