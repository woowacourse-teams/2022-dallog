package com.allog.dallog.domain.auth.domain;

public class DallogToken {

    private String accessToken;
    private String refreshToken;

    public DallogToken(final String accessToken, final String refreshToken) {
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
