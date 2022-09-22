package com.allog.dallog.domain.category.exception;

public class ExistExternalCategoryException extends RuntimeException {

    public ExistExternalCategoryException(final String message) {
        super(message);
    }

    public ExistExternalCategoryException() {
        this("이미 저장된 연동 카테고리입니다.");
    }
}
