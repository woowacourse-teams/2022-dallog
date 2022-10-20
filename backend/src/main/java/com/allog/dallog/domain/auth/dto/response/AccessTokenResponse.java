package com.allog.dallog.domain.auth.dto.response;

public class AccessTokenResponse {

    private String accessToken;

    private AccessTokenResponse() {
    }

    public AccessTokenResponse(final String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
