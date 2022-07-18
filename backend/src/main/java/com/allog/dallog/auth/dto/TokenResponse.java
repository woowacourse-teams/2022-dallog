package com.allog.dallog.auth.dto;

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
