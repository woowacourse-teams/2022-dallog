package com.allog.dallog.categoryrole.exception;

public class ManagingCategoryLimitExcessException extends RuntimeException {

    private static final int MAX_MANAGING_CATEGORY_COUNT = 50;

    public ManagingCategoryLimitExcessException() {
        super("한 사람이 관리할 수 있는 카테고리는 최대 " + MAX_MANAGING_CATEGORY_COUNT + "개 입니다.");
    }
}
