package com.allog.dallog.auth.exception;

public class NotFoundMemberException extends RuntimeException {

    public NotFoundMemberException(final String message) {
        super(message);
    }

    public NotFoundMemberException() {
        this("존재하지 않는 데이터 입니다.");
    }
}
