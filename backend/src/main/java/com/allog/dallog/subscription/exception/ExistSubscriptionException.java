package com.allog.dallog.subscription.exception;

public class ExistSubscriptionException extends RuntimeException {

    public ExistSubscriptionException(final String message) {
        super(message);
    }

    public ExistSubscriptionException() {
        this("이미 존재하는 구독 정보 입니다.");
    }
}
