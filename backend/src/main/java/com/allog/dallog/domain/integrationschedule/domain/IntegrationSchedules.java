package com.allog.dallog.domain.integrationschedule.domain;

import java.util.ArrayList;
import java.util.List;

public class IntegrationSchedules {

    private static final IntegrationScheduleComparator COMPARATOR = new IntegrationScheduleComparator();

    private final List<IntegrationSchedule> values;

    public IntegrationSchedules() {
        this.values = new ArrayList<>();
    }

    public void add(final IntegrationSchedule integrationSchedule) {
        values.add(integrationSchedule);
    }

    public List<IntegrationSchedule> getSortedValues() {
        values.sort(COMPARATOR);
        return List.copyOf(values);
    }
}
