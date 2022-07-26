package com.allog.dallog.domain.schedule.exception;

public class InvalidScheduleException extends RuntimeException {

    public InvalidScheduleException(final String message) {
        super(message);
    }

    public InvalidScheduleException() {
        this("잘못된 일정입니다.");
    }
}
