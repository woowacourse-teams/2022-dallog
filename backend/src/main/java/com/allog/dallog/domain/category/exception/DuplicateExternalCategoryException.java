package com.allog.dallog.domain.category.exception;

public class DuplicateExternalCategoryException extends RuntimeException {

    public DuplicateExternalCategoryException(final String message) {
        super(message);
    }

    public DuplicateExternalCategoryException() {
        this("이미 저장된 연동 카테고리입니다.");
    }
}
