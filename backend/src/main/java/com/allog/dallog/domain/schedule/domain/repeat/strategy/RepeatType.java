package com.allog.dallog.domain.schedule.domain.repeat.strategy;

import com.allog.dallog.domain.schedule.exception.InvalidRepeatTypeException;
import java.util.Arrays;

public enum RepeatType {
    EVERY_DAY("everyDay", new EveryDayRepeatStrategy()),
    EVERY_WEEK("everyWeek", new EveryWeekRepeatStrategy()),
    EVERY_MONTH("everyMonth", new EveryMonthRepeatStrategy()),
    ;

    private final String name;
    private final RepeatStrategy repeatStrategy;

    RepeatType(final String name, final RepeatStrategy repeatStrategy) {
        this.name = name;
        this.repeatStrategy = repeatStrategy;
    }

    public static RepeatType from(final String name) {
        return Arrays.stream(values())
                .filter(repeatType -> repeatType.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new InvalidRepeatTypeException(name));
    }

    public String getName() {
        return name;
    }

    public RepeatStrategy getRepeatStrategy() {
        return repeatStrategy;
    }
}
