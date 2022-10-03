package com.allog.dallog.domain.categoryrole.exception;

public class NotAbleToMangeRoleException extends RuntimeException {

    public NotAbleToMangeRoleException(final String message) {
        super(message);
    }

    public NotAbleToMangeRoleException() {
        super("역할을 변경할 수 없습니다.");
    }
}
