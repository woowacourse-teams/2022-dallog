package com.allog.dallog.domain.schedule.exception;

public class InvalidRepeatTypeException extends RuntimeException {

    public InvalidRepeatTypeException(final String name) {
        super(name + "은 잘못된 일정 반복 타입입니다.");
    }
}
