package com.allog.dallog.domain.schedule.domain.schedules;

import com.allog.dallog.domain.schedule.domain.Schedule;
import java.util.Arrays;
import java.util.function.Predicate;

public enum ScheduleType {

    LONG_TERMS("longTerms", schedule ->
            (schedule.calculateHourDifference() > 24) ||
                    (schedule.calculateHourDifference() < 24 && schedule.isDayDifferent())
    ),
    ALL_DAYS("allDays", schedule -> schedule.calculateHourDifference() == 24 && schedule.isMidNightToMidNight()),
    FEW_HOURS("fewHours", schedule -> schedule.calculateHourDifference() < 24 && !schedule.isDayDifferent());

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
