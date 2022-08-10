package com.allog.dallog.domain.schedule.domain.schedules;

import com.allog.dallog.domain.schedule.domain.Schedule;
import java.util.Arrays;
import java.util.function.Predicate;

public enum ScheduleType {

    LONG_TERMS("longTerms", Schedule::isLongTerms),
    ALL_DAYS("allDays", Schedule::isAllDays),
    FEW_HOURS("fewHours", Schedule::isFewHours);

    private final String name;
    private final Predicate<Schedule> isMatch;

    ScheduleType(final String name, final Predicate<Schedule> isMatch) {
        this.name = name;
        this.isMatch = isMatch;
    }

    public static ScheduleType from(final Schedule schedule) {
        return Arrays.stream(values())
                .filter(type -> type.isMatch.test(schedule))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public String getName() {
        return name;
    }
}
