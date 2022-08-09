package com.allog.dallog.domain.schedule.domain.repeat.strategy;

import com.allog.dallog.domain.schedule.domain.Schedule;

public class EveryWeekRepeatStrategy extends RepeatStrategy {

    private static final int REPEAT_WEEK_UNIT = 1;

    @Override
    protected Schedule generateNextSchedule(final Schedule previousSchedule) {
        return previousSchedule.plusWeeks(REPEAT_WEEK_UNIT);
    }
}
