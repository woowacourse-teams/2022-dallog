package com.allog.dallog.domain.auth.dto.response;

public class AccessAndRefreshTokenResponse {

    private String accessToken;
    private String refreshToken;

    private AccessAndRefreshTokenResponse() {
    }

    public AccessAndRefreshTokenResponse(final String accessToken, final String refreshToken) {
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
