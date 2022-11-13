package com.allog.dallog.categoryrole.exception;

public class NoSuchCategoryRoleException extends RuntimeException {

    public NoSuchCategoryRoleException(final String message) {
        super(message);
    }

    public NoSuchCategoryRoleException() {
        this("존재하지 않는 역할입니다.");
    }
}
