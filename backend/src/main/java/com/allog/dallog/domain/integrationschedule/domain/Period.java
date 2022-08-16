package com.allog.dallog.domain.integrationschedule.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Period {

    private static final int ONE_HOUR = 60;

    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    public Period(final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
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
}
