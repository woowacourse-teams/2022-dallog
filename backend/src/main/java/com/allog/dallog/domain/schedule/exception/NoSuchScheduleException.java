package com.allog.dallog.domain.schedule.exception;

public class NoSuchScheduleException extends RuntimeException {

    public NoSuchScheduleException(final String message) {
        super(message);
    }

    public NoSuchScheduleException() {
        this("존재하지 않는 일정입니다.");
    }
}
