package com.allog.dallog.domain.schedule.domain.repeat;

import com.allog.dallog.domain.schedule.domain.Schedule;
import com.allog.dallog.domain.schedule.domain.repeat.strategy.RepeatStrategy;
import com.allog.dallog.domain.schedule.domain.repeat.strategy.RepeatType;
import java.time.LocalDate;
import java.util.List;

public class ScheduleRepeatGroup {

    private final List<Schedule> schedules;

    public ScheduleRepeatGroup(final Schedule initialSchedule, final LocalDate endDate, final RepeatType repeatType) {
        RepeatStrategy repeatStrategy = repeatType.getRepeatStrategy();
        this.schedules = repeatStrategy.getSchedules(initialSchedule, endDate);
    }

    public List<Schedule> getSchedules() {
        return List.copyOf(schedules);
    }
}
