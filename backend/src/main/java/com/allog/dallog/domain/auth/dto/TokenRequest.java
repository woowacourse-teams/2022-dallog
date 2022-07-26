package com.allog.dallog.domain.auth.dto;

public class TokenRequest {

    private String code;

    private TokenRequest() {
    }

    public TokenRequest(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
