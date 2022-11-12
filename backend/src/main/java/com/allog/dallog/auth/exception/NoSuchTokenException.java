package com.allog.dallog.auth.exception;

public class NoSuchTokenException extends RuntimeException {

    public NoSuchTokenException(final String message) {
        super(message);
    }

    public NoSuchTokenException() {
        this("존재하지 않는 Token 입니다.");
    }
}
