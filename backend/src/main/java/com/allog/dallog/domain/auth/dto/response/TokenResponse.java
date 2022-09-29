package com.allog.dallog.domain.auth.dto.response;

public class TokenResponse {

    private String accessToken;
    private String refreshToken;

    private TokenResponse() {
    }

    public TokenResponse(final String accessToken, final String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
