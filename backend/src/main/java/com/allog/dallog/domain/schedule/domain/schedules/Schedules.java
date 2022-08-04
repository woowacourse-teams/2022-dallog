package com.allog.dallog.domain.schedule.domain.schedules;

import com.allog.dallog.domain.schedule.domain.Schedule;
import java.util.ArrayList;
import java.util.List;

public class Schedules {

    private static final ScheduleComparator COMPARATOR = new ScheduleComparator();

    private final List<Schedule> values;

    public Schedules() {
        this.values = new ArrayList<>();
    }

    public void add(final Schedule schedule) {
        values.add(schedule);
    }

    public List<Schedule> getSortedValues() {
        values.sort(COMPARATOR);
        return List.copyOf(values);
    }
}
