package com.allog.dallog.infrastructure.oauth.exception;

public class OAuthException extends RuntimeException {

    public OAuthException(final Exception e) {
        super(e);
    }

    public OAuthException(final String message) {
        super(message);
    }

    public OAuthException() {
        this("Oauth 서버와의 통신 과정에서 문제가 발생했습니다.");
    }
}
