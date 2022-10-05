package com.allog.dallog.domain.auth.dto.response;

import java.util.Objects;

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
