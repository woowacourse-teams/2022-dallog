package com.allog.dallog.domain.schedule.domain.schedules;

import com.allog.dallog.domain.schedule.domain.Schedule;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypedSchedules {

    private Map<ScheduleType, Schedules> values;

    public TypedSchedules(final List<Schedule> schedules) {
        initializeValues();
        for (Schedule schedule : schedules) {
            ScheduleType scheduleType = ScheduleType.from(schedule);
            Schedules sortedSchedules = values.get(scheduleType);
            sortedSchedules.add(schedule);
        }
    }

    private void initializeValues() {
        this.values = new HashMap<>();
        for (ScheduleType type : ScheduleType.values()) {
            values.put(type, new Schedules());
        }
    }

    public Schedules getSortedSchedules(final ScheduleType scheduleType) {
        return values.get(scheduleType);
    }
}
