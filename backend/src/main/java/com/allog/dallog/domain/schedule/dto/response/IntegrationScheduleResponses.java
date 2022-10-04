package com.allog.dallog.domain.schedule.dto.response;

import com.allog.dallog.domain.schedule.domain.ScheduleType;
import com.allog.dallog.domain.schedule.domain.TypedSchedules;
import com.allog.dallog.domain.subscription.domain.Color;
import com.allog.dallog.domain.subscription.domain.Subscriptions;
import java.util.List;
import java.util.stream.Collectors;

public class IntegrationScheduleResponses {

    private final List<IntegrationScheduleResponse> longTerms;
    private final List<IntegrationScheduleResponse> allDays;
    private final List<IntegrationScheduleResponse> fewHours;

    public IntegrationScheduleResponses(final List<IntegrationScheduleResponse> longTerms,
                                        final List<IntegrationScheduleResponse> allDays,
                                        final List<IntegrationScheduleResponse> fewHours) {
        this.longTerms = longTerms;
        this.allDays = allDays;
        this.fewHours = fewHours;
    }

    public IntegrationScheduleResponses(final Subscriptions subscriptions, final TypedSchedules typedSchedules) {
        this.longTerms = getColoredScheduleResponses(ScheduleType.LONG_TERMS, subscriptions, typedSchedules);
        this.allDays = getColoredScheduleResponses(ScheduleType.ALL_DAYS, subscriptions, typedSchedules);
        this.fewHours = getColoredScheduleResponses(ScheduleType.FEW_HOURS, subscriptions, typedSchedules);
    }

    public IntegrationScheduleResponses(final Color color, final TypedSchedules typedSchedules) {
        this.longTerms = getColoredScheduleResponses(ScheduleType.LONG_TERMS, color, typedSchedules);
        this.allDays = getColoredScheduleResponses(ScheduleType.ALL_DAYS, color, typedSchedules);
        this.fewHours = getColoredScheduleResponses(ScheduleType.FEW_HOURS, color, typedSchedules);
    }

    private List<IntegrationScheduleResponse> getColoredScheduleResponses(final ScheduleType scheduleType,
                                                                          final Subscriptions subscriptions,
                                                                          final TypedSchedules typedSchedules) {
        return typedSchedules.getSortedSchedules(scheduleType)
                .getSortedValues()
                .stream()
                .map(schedule -> new IntegrationScheduleResponse(schedule, subscriptions.findColor(schedule)))
                .collect(Collectors.toList());
    }

    private List<IntegrationScheduleResponse> getColoredScheduleResponses(final ScheduleType scheduleType,
                                                                          final Color color,
                                                                          final TypedSchedules typedSchedules) {
        return typedSchedules.getSortedSchedules(scheduleType)
                .getSortedValues()
                .stream()
                .map(schedule -> new IntegrationScheduleResponse(schedule, color))
                .collect(Collectors.toList());
    }

    public List<IntegrationScheduleResponse> getLongTerms() {
        return longTerms;
    }

    public List<IntegrationScheduleResponse> getAllDays() {
        return allDays;
    }

    public List<IntegrationScheduleResponse> getFewHours() {
        return fewHours;
    }
}
