package com.allog.dallog.domain.auth.dto.response;

import java.util.Objects;

public class TokenRenewalResponse {

    private String accessToken;

    private TokenRenewalResponse() {
    }

    public TokenRenewalResponse(final String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
