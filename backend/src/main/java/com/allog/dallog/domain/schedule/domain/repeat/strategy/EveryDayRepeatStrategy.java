package com.allog.dallog.domain.schedule.domain.repeat.strategy;

import com.allog.dallog.domain.schedule.domain.Schedule;

public class EveryDayRepeatStrategy extends RepeatStrategy {

    private static final int REPEAT_DAY_UNIT = 1;

    @Override
    protected Schedule generateNextSchedule(final Schedule previousSchedule) {
        return previousSchedule.plusDays(REPEAT_DAY_UNIT);
    }
}
