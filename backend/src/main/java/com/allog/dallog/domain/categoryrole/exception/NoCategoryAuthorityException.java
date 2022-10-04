package com.allog.dallog.domain.categoryrole.exception;

import com.allog.dallog.domain.categoryrole.domain.CategoryAuthority;

public class NoCategoryAuthorityException extends RuntimeException {

    public NoCategoryAuthorityException(final String message) {
        super(message);
    }

    public NoCategoryAuthorityException(final CategoryAuthority authority) {
        this(authority.getName() + " 권한이 없습니다.");
    }
}
