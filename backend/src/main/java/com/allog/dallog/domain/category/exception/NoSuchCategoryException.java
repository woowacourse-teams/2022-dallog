package com.allog.dallog.domain.category.exception;

public class NoSuchCategoryException extends RuntimeException {

    public NoSuchCategoryException(final String message) {
        super(message);
    }

    public NoSuchCategoryException() {
        this("존재하지 않는 카테고리입니다.");
    }
}
