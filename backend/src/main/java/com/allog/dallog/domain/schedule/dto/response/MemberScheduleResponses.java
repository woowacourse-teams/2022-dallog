package com.allog.dallog.domain.schedule.dto.response;

import com.allog.dallog.domain.schedule.domain.ScheduleType;
import com.allog.dallog.domain.schedule.domain.TypedSchedules;
import com.allog.dallog.domain.subscription.domain.Color;
import com.allog.dallog.domain.subscription.domain.Subscriptions;
import java.util.List;
import java.util.stream.Collectors;

public class MemberScheduleResponses {

    private final List<MemberScheduleResponse> longTerms;
    private final List<MemberScheduleResponse> allDays;
    private final List<MemberScheduleResponse> fewHours;

    public MemberScheduleResponses(final List<MemberScheduleResponse> longTerms,
                                   final List<MemberScheduleResponse> allDays,
                                   final List<MemberScheduleResponse> fewHours) {
        this.longTerms = longTerms;
        this.allDays = allDays;
        this.fewHours = fewHours;
    }

    public MemberScheduleResponses(final Subscriptions subscriptions, final TypedSchedules typedSchedules) {
        this.longTerms = getColoredScheduleResponses(ScheduleType.LONG_TERMS, subscriptions, typedSchedules);
        this.allDays = getColoredScheduleResponses(ScheduleType.ALL_DAYS, subscriptions, typedSchedules);
        this.fewHours = getColoredScheduleResponses(ScheduleType.FEW_HOURS, subscriptions, typedSchedules);
    }

    public MemberScheduleResponses(final Color color, final TypedSchedules typedSchedules) {
        this.longTerms = getColoredScheduleResponses(ScheduleType.LONG_TERMS, color, typedSchedules);
        this.allDays = getColoredScheduleResponses(ScheduleType.ALL_DAYS, color, typedSchedules);
        this.fewHours = getColoredScheduleResponses(ScheduleType.FEW_HOURS, color, typedSchedules);
    }

    private List<MemberScheduleResponse> getColoredScheduleResponses(final ScheduleType scheduleType,
                                                                     final Subscriptions subscriptions,
                                                                     final TypedSchedules typedSchedules) {
        return typedSchedules.getSortedSchedules(scheduleType)
                .getSortedValues()
                .stream()
                .map(schedule -> new MemberScheduleResponse(schedule, subscriptions.findColor(schedule)))
                .collect(Collectors.toList());
    }

    private List<MemberScheduleResponse> getColoredScheduleResponses(final ScheduleType scheduleType, final Color color,
                                                                     final TypedSchedules typedSchedules) {
        return typedSchedules.getSortedSchedules(scheduleType)
                .getSortedValues()
                .stream()
                .map(schedule -> new MemberScheduleResponse(schedule, color))
                .collect(Collectors.toList());
    }

    public List<MemberScheduleResponse> getLongTerms() {
        return longTerms;
    }

    public List<MemberScheduleResponse> getAllDays() {
        return allDays;
    }

    public List<MemberScheduleResponse> getFewHours() {
        return fewHours;
    }
}
