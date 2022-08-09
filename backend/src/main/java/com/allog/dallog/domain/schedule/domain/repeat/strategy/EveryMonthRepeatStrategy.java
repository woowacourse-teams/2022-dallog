package com.allog.dallog.domain.schedule.domain.repeat.strategy;

import com.allog.dallog.domain.schedule.domain.Schedule;

public class EveryMonthRepeatStrategy extends RepeatStrategy {

    private static final int REPEAT_MONTH_UNIT = 1;

    @Override
    protected Schedule generateNextSchedule(final Schedule previousSchedule) {
        return previousSchedule.plusMonths(REPEAT_MONTH_UNIT);
    }
}
