package com.allog.dallog.category.domain;

import com.allog.dallog.category.exception.NoSuchCategoryException;

public enum CategoryType {

    NORMAL, PERSONAL, GOOGLE;

    public static CategoryType from(final String value) {
        try {
            return CategoryType.valueOf(value.toUpperCase());
        } catch (final IllegalArgumentException e) {
            throw new NoSuchCategoryException("(" + value + ")는 존재하지 않는 카테고리 타입입니다.");
        }
    }
}
