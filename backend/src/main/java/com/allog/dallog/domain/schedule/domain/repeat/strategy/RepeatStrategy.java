package com.allog.dallog.domain.schedule.domain.repeat.strategy;

import com.allog.dallog.domain.schedule.domain.Schedule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public abstract class RepeatStrategy {

    public List<Schedule> getSchedules(final Schedule initialSchedule, final LocalDate endDate) {
        List<Schedule> schedules = new ArrayList<>();
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);

        Schedule currentSchedule = initialSchedule;
        while (currentSchedule.getStartDateTime().isBefore(endDateTime)) {
            schedules.add(currentSchedule);
            currentSchedule = generateNextSchedule(currentSchedule);
        }

        return schedules;
    }

    abstract protected Schedule generateNextSchedule(Schedule previousSchedule);
}
