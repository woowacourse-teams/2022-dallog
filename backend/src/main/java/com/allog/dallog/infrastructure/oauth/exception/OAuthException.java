package com.allog.dallog.infrastructure.oauth.exception;

public class OAuthException extends RuntimeException {

    public OAuthException() {
        super("Oauth 서버와의 통신 과정에서 문제가 발생했습니다.");
    }

    public OAuthException(final Exception e) {
        this("Oauth 서버와의 통신 과정에서 문제가 발생했습니다.", e);
    }

    public OAuthException(final String message, final Exception e) {
        super(message, e);
    }
}
