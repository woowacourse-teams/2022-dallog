package com.allog.dallog.domain.auth.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OAuthAccessTokenResponse {

    private String value;

    private OAuthAccessTokenResponse() {
    }

    public OAuthAccessTokenResponse(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
