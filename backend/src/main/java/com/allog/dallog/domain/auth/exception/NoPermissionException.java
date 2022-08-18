package com.allog.dallog.domain.auth.exception;

public class NoPermissionException extends RuntimeException {

    public NoPermissionException(final String message) {
        super(message);
    }

    public NoPermissionException() {
        this("권한이 없는 요청 입니다.");
    }
}
