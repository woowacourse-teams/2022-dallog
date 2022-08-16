package com.allog.dallog.domain.integrationschedule.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
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

    public Period plusMinutes(final int minutes) {
        LocalDateTime startDateTime = this.startDateTime.plusMinutes(minutes);
        LocalDateTime endDateTime = this.endDateTime.plusMinutes(minutes);
        return new Period(startDateTime, endDateTime);
    }

    public List<Period> slice(final Period otherPeriod) {
        if (isNotOverlapped(otherPeriod)) {
            return List.of(this);
        }

        return sliceByOtherPeriod(otherPeriod);
    }

    private List<Period> sliceByOtherPeriod(final Period otherPeriod) {
        List<Period> periods = new ArrayList<>();
        if (startDateTime.isBefore(otherPeriod.startDateTime)) {
            periods.add(new Period(startDateTime, otherPeriod.startDateTime));
        }

        if (otherPeriod.endDateTime.isBefore(endDateTime)) {
            periods.add(new Period(otherPeriod.endDateTime, endDateTime));
        }

        return periods;
    }

    private boolean isNotOverlapped(final Period otherPeriod) {
        return otherPeriod.endDateTime.isBefore(startDateTime) || endDateTime.isBefore(otherPeriod.startDateTime);
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
