package com.allog.dallog.domain.auth.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TokenRenewalRequest {

    @NotBlank(message = "엑세스 토큰은 공백일 수 없습니다.")
    private String accessToken;

    @NotNull(message = "리프레시 토큰은 공백일 수 없습니다.")
    private String refreshToken;

    private TokenRenewalRequest() {
    }

    public TokenRenewalRequest(final String accessToken, final String refreshToken) {
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
