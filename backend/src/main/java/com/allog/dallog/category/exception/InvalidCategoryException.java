package com.allog.dallog.category.exception;

public class InvalidCategoryException extends RuntimeException {

    public InvalidCategoryException(final String message) {
        super(message);
    }

    public InvalidCategoryException() {
        this("잘못된 카테고리입니다.");
    }
}
