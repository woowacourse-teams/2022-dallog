package com.allog.dallog.domain.schedule.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum ScheduleType {

    LONG_TERMS("longTerms", IntegrationSchedule::isLongTerms),
    ALL_DAYS("allDays", IntegrationSchedule::isAllDays),
    FEW_HOURS("fewHours", IntegrationSchedule::isFewHours);

    private final String name;
    private final Predicate<IntegrationSchedule> isMatch;

    ScheduleType(final String name, final Predicate<IntegrationSchedule> isMatch) {
        this.name = name;
        this.isMatch = isMatch;
    }

    public static ScheduleType from(final IntegrationSchedule integrationSchedule) {
        return Arrays.stream(values())
                .filter(type -> type.isMatch.test(integrationSchedule))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("일치하는 일정 종류가 존재하지 않습니다."));
    }

    public String getName() {
        return name;
    }
}
