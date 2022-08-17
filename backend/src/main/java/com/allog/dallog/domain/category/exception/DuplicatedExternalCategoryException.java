package com.allog.dallog.domain.category.exception;

public class DuplicatedExternalCategoryException extends RuntimeException {


    public DuplicatedExternalCategoryException(final String message) {
        super(message);
    }

    public DuplicatedExternalCategoryException() {
        this("이미 저장된 연동 카테고리입니다.");
    }
}
