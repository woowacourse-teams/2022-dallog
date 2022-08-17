package com.allog.dallog.domain.schedule.domain.scheduler;

import com.allog.dallog.domain.integrationschedule.domain.IntegrationSchedule;
import com.allog.dallog.domain.integrationschedule.domain.Period;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    private final List<IntegrationSchedule> schedules;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Scheduler(final List<IntegrationSchedule> schedules, final LocalDate startDate, final LocalDate endDate) {
        this.schedules = schedules;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public List<Period> getPeriods() {
        List<Period> periods = new ArrayList<>();
        periods.add(generateBasePeriod());

        for (IntegrationSchedule schedule : schedules) {
            slicePeriod(periods, schedule);
        }

        return periods;
    }

    private Period generateBasePeriod() {
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        return new Period(startDateTime, endDateTime);
    }

    private void slicePeriod(final List<Period> periods, final IntegrationSchedule schedule) {
        for (Period period : List.copyOf(periods)) {
            periods.remove(period);
            periods.addAll(period.slice(schedule.getPeriod()));
        }
    }
}
