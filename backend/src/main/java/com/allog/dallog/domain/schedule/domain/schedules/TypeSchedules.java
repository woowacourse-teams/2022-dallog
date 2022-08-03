package com.allog.dallog.domain.schedule.domain.schedules;

import com.allog.dallog.domain.schedule.domain.Schedule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeSchedules {

    private final Map<ScheduleType, SortedSchedules> values;

    public TypeSchedules() {
        values = new HashMap<>();
        for (ScheduleType type : ScheduleType.values()) {
            values.put(type, new SortedSchedules());
        }
    }

    public void add(final Schedule schedule) {
        ScheduleType type = ScheduleType.getScheduleType(schedule);
        SortedSchedules sortedSchedules = values.get(type);
        sortedSchedules.add(schedule);
    }
}
