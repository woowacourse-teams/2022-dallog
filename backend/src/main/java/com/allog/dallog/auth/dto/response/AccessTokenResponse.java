package com.allog.dallog.auth.dto.response;

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
