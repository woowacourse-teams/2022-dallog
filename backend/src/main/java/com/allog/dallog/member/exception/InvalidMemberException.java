package com.allog.dallog.member.exception;

public class InvalidMemberException extends RuntimeException {

    public InvalidMemberException(final String message) {
        super(message);
    }

    public InvalidMemberException() {
        super("잘못된 회원의 정보입니다.");
    }
}
