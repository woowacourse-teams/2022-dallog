package com.allog.dallog.domain.auth.dto.request;

public class TokenRequest {

    private String code;
    private String redirectUri;

    private TokenRequest() {
    }

    public TokenRequest(final String code, final String redirectUri) {
        this.code = code;
        this.redirectUri = redirectUri;
    }

    public String getCode() {
        return code;
    }

    public String getRedirectUri() {
        return redirectUri;
    }
}
