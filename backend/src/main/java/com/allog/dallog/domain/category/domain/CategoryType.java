package com.allog.dallog.domain.category.domain;

import com.allog.dallog.domain.category.exception.InvalidCategoryException;

public enum CategoryType {

    NORMAL, PERSONAL, GOOGLE;

    public static CategoryType from(final String value) {
        try {
            return CategoryType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCategoryException("(" + value + ")는 존재하지 않는 카테고리 타입입니다.");
        }
    }
}
