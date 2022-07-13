package com.allog.dallog.category.exception;

public class InvalidCategoryException extends RuntimeException {

    public InvalidCategoryException() {
        super("잘못된 카테고리입니다.");
    }

    public InvalidCategoryException(final String message) {
        super(message);
    }
}
