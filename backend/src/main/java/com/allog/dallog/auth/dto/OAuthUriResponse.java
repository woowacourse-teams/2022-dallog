package com.allog.dallog.auth.dto;

// OAuth 인증 URI(소셜 로그인 링크)를 전달하는 DTO
public class OAuthUriResponse {

    private String oAuthUri;

    private OAuthUriResponse() {
    }

    public OAuthUriResponse(final String oAuthUri) {
        this.oAuthUri = oAuthUri;
    }

    public String getoAuthUri() {
        return oAuthUri;
    }
}
