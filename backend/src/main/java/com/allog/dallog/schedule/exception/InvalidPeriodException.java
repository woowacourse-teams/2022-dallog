package com.allog.dallog.schedule.exception;

public class InvalidPeriodException extends RuntimeException {

    public InvalidPeriodException() {
        super("잘못된 기간입니다.");
    }

    public InvalidPeriodException(final String message) {
        super(message);
    }
}
