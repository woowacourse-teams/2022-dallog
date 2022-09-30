package com.allog.dallog.domain.subscription.exception;

public class NotAbleToUnsubscribeException extends RuntimeException {

    public NotAbleToUnsubscribeException(final String message) {
        super(message);
    }

    public NotAbleToUnsubscribeException() {
        this("구독 해제할 수 없습니다.");
    }
}
