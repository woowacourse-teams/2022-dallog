package com.allog.dallog.domain.subscription.exception;

public class InvalidSubscriptionException extends RuntimeException {

    public InvalidSubscriptionException(final String message) {
        super(message);
    }

    public InvalidSubscriptionException() {
        this("유효하지 않은 구독 정보입니다.");
    }
}
