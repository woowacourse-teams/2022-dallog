package com.allog.dallog.domain.schedule.domain.schedules;

import com.allog.dallog.domain.schedule.domain.Schedule;
import java.util.ArrayList;
import java.util.List;

public class SortedSchedules {

    private final List<Schedule> values;

    public SortedSchedules() {
        this.values = new ArrayList<>();
    }

    public void add(final Schedule schedule) {
        values.add(schedule);
        values.sort(new ScheduleComparator());
    }

    public List<Schedule> getValues() {
        return List.copyOf(values);
    }
}
