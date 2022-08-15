package com.allog.dallog.domain.integrationschedule.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

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
