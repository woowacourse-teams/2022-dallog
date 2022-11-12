package com.allog.dallog.categoryrole.exception;

public class NotAbleToChangeRoleException extends RuntimeException {

    public NotAbleToChangeRoleException(final String message) {
        super(message);
    }

    public NotAbleToChangeRoleException() {
        super("역할을 변경할 수 없습니다.");
    }

    public static NotAbleToChangeRoleException concurrentIssue() {
        return new NotAbleToChangeRoleException("회원님의 권한이 변경되어 카테고리 역할을 수정할 수 없습니다.");
    }
}
