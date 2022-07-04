package com.allog.dallog.schedule.domain;

import java.time.LocalDateTime;

public class Period {

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public Period(final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        validate(startDateTime, endDateTime);

        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    private void validate(final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        if (startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("종료일시가 시작일시보다 이전일 수 없습니다.");
        }
    }
}
