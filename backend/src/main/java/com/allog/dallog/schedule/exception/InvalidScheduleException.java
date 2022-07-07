package com.allog.dallog.schedule.exception;

public class InvalidScheduleException extends RuntimeException {

    public InvalidScheduleException() {
        super("잘못된 일정입니다.");
    }

    public InvalidScheduleException(final String message) {
        super(message);
    }
}
