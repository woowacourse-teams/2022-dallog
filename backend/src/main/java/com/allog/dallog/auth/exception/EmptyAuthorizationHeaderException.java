package com.allog.dallog.auth.exception;

public class EmptyAuthorizationHeaderException extends RuntimeException {

    public EmptyAuthorizationHeaderException(final String message) {
        super(message);
    }

    public EmptyAuthorizationHeaderException() {
        this("header에 Authorization이 존재하지 않습니다.");
    }
}
