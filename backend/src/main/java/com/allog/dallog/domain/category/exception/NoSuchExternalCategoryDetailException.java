package com.allog.dallog.domain.category.exception;

public class NoSuchExternalCategoryDetailException extends RuntimeException {

    public NoSuchExternalCategoryDetailException(final String message) {
        super(message);
    }

    public NoSuchExternalCategoryDetailException() {
        this("존재하지 않는 외부 카테고리 정보 입니다.");
    }
}
