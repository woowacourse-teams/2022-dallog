package com.allog.dallog.domain.schedule.domain.repeat.strategy;

import com.allog.dallog.domain.schedule.domain.Schedule;
import java.time.LocalDate;
import java.util.List;

@FunctionalInterface
public interface RepeatStrategy {

    List<Schedule> getSchedules(final Schedule initialSchedule, final LocalDate endDate);
}
