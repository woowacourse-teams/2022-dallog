package com.allog.dallog.domain.schedule.domain.repeat;

import com.allog.dallog.domain.schedule.exception.InvalidRepeatTypeException;
import java.util.Arrays;

public enum RepeatType {
    EVERY_DAY("everyDay"),
    EVERY_WEEK("everyWeek"),
    EVERY_MONTH("everyMonth"),
    ;

    private final String name;

    RepeatType(final String name) {
        this.name = name;
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
}
