package com.allog.dallog.domain.schedule.domain.scheduler;

import com.allog.dallog.domain.integrationschedule.domain.IntegrationSchedule;
import com.allog.dallog.domain.integrationschedule.domain.Period;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    private final List<IntegrationSchedule> schedules;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    public Scheduler(final List<IntegrationSchedule> schedules, final LocalDateTime startDateTime,
                     final LocalDateTime endDate) {
        this.schedules = schedules;
        this.startDateTime = startDateTime;
        this.endDateTime = endDate;
    }

    public List<Period> getPeriods() {
        List<Period> periods = new ArrayList<>();
        Period initialBasePeriod = new Period(startDateTime, endDateTime);
        periods.add(initialBasePeriod);

        for (IntegrationSchedule schedule : schedules) {
            slicePeriod(periods, schedule);
        }

        return periods;
    }

    private void slicePeriod(final List<Period> periods, final IntegrationSchedule schedule) {
        for (Period period : List.copyOf(periods)) {
            periods.remove(period);
            periods.addAll(period.slice(schedule.getPeriod()));
        }
    }
}
