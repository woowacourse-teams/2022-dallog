package com.allog.dallog.domain.categoryrole.exception;

public class CategoryRoleConcurrencyException extends RuntimeException {

    public CategoryRoleConcurrencyException() {
        super("누군가 회원님의 카테고리 역할을 수정했습니다. 다시 시도해주세요.");
    }
}
