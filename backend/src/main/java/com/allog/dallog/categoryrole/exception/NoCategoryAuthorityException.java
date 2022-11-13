package com.allog.dallog.categoryrole.exception;

public class NoCategoryAuthorityException extends RuntimeException {

    public NoCategoryAuthorityException(final String authorityName) {
        super(authorityName + " 권한이 없습니다.");
    }
}
