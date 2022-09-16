package com.allog.dallog.domain.integrationschedule.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypedSchedules {

    private Map<ScheduleType, IntegrationSchedules> values;

    public TypedSchedules(final List<IntegrationSchedule> integrationSchedules) {
        initializeValues();
        for (IntegrationSchedule integrationSchedule : integrationSchedules) {
            ScheduleType scheduleType = ScheduleType.from(integrationSchedule);
            IntegrationSchedules schedules = values.get(scheduleType);
            schedules.add(integrationSchedule);
        }
    }

    private void initializeValues() {
        this.values = new HashMap<>();
        for (ScheduleType type : ScheduleType.values()) {
            values.put(type, new IntegrationSchedules());
        }
    }

    public IntegrationSchedules getSortedSchedules(final ScheduleType scheduleType) {
        return values.get(scheduleType);
    }
}
