package com.allog.dallog.domain.auth.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TokenCreateRequest {

    @NotBlank(message = "인가 코드는 공백일 수 없습니다.")
    private String code;

    @NotNull(message = "Null일 수 없습니다.")
    private String redirectUri;

    private TokenCreateRequest() {
    }

    public TokenCreateRequest(final String code, final String redirectUri) {
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
