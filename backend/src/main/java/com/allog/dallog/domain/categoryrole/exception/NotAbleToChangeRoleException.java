package com.allog.dallog.domain.categoryrole.exception;

public class NotAbleToChangeRoleException extends RuntimeException {

    public NotAbleToChangeRoleException(final String message) {
        super(message);
    }

    public NotAbleToChangeRoleException() {
        super("역할을 변경할 수 없습니다.");
    }
}
