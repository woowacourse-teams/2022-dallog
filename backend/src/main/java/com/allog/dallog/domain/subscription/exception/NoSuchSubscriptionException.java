package com.allog.dallog.domain.subscription.exception;

public class NoSuchSubscriptionException extends RuntimeException {

    public NoSuchSubscriptionException(final String message) {
        super(message);
    }

    public NoSuchSubscriptionException() {
        this("존재하지 않는 구독 정보입니다.");
    }
}
