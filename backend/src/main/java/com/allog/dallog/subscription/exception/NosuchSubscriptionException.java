package com.allog.dallog.subscription.exception;

public class NosuchSubscriptionException extends RuntimeException {

    public NosuchSubscriptionException(final String message) {
        super(message);
    }

    public NosuchSubscriptionException() {
        this("존재하지 않는 구독 정보입니다.");
    }
}
