package com.allog.dallog.domain.categoryrole.exception;

public class NoPermissionToManageRoleException extends RuntimeException {

    public NoPermissionToManageRoleException(final String message) {
        super(message);
    }

    public NoPermissionToManageRoleException() {
        this("역할을 관리할 권한이 없습니다.");
    }
}
