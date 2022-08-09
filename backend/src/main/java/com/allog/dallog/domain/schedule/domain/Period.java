package com.allog.dallog.domain.schedule.domain;

import com.allog.dallog.domain.schedule.exception.InvalidScheduleException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Period {

    private static final int ONE_HOUR = 60;

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
            throw new InvalidScheduleException("종료일시가 시작일시보다 이전일 수 없습니다.");
        }
    }

    public long calculateDayDifference() {
        LocalDate startDate = LocalDate.from(startDateTime);
        LocalDate endDate = LocalDate.from(endDateTime);
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    public long calculateHourDifference() {
        LocalTime startTime = LocalTime.from(startDateTime);
        LocalTime endTime = LocalTime.from(endDateTime);
        return ChronoUnit.HOURS.between(startTime, endTime);
    }

    public long calculateMinuteDifference() {
        LocalTime startTime = LocalTime.from(startDateTime);
        LocalTime endTime = LocalTime.from(endDateTime);
        return ChronoUnit.MINUTES.between(startTime, endTime) % ONE_HOUR;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Period period = (Period) o;
        return Objects.equals(startDateTime, period.startDateTime) && Objects.equals(endDateTime, period.endDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDateTime, endDateTime);
    }
}
