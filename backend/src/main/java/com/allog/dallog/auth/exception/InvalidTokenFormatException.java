package com.allog.dallog.auth.exception;

public class InvalidTokenFormatException extends RuntimeException {

    public InvalidTokenFormatException(final String message) {
        super(message);
    }

    public InvalidTokenFormatException() {
        this("token 형식이 잘못 되었습니다. (형식: Bearer aaaaaaaa.bbbbbbbb.cccccccc)");
    }
}
