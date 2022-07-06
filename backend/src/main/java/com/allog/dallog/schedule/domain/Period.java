package com.allog.dallog.schedule.domain;

import com.allog.dallog.schedule.exception.InvalidPeriodException;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Period {

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    protected Period() {
    }

    public Period(final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        validate(startDateTime, endDateTime);

        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    private void validate(final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        if (startDateTime.isAfter(endDateTime)) {
            throw new InvalidPeriodException("종료일시가 시작일시보다 이전일 수 없습니다.");
        }
    }
}
