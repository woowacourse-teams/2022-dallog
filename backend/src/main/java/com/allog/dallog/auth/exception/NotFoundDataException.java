package com.allog.dallog.auth.exception;

public class NotFoundDataException extends RuntimeException {

    public NotFoundDataException(final String message) {
        super(message);
    }

    public NotFoundDataException() {
        this("존재하지 않는 데이터 입니다.");
    }
}
