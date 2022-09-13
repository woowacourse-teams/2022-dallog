package com.allog.dallog.domain.auth.dto.response;

// OAuth 인증 URI(소셜 로그인 링크)를 전달하는 DTO
public class OAuthLinkResponse {

    private String oAuthUri;

    private OAuthLinkResponse() {
    }

    public OAuthLinkResponse(final String oAuthUri) {
        this.oAuthUri = oAuthUri;
    }

    public String getoAuthUri() {
        return oAuthUri;
    }
}
