package com.allog.dallog.domain.schedule.domain.schedules;

import com.allog.dallog.domain.schedule.domain.Schedule;
import com.allog.dallog.domain.schedule.domain.schedules.comparator.LongTermComparator;
import java.util.ArrayList;
import java.util.List;

public class LongTerms {

    private final List<Schedule> schedules = new ArrayList<>();

    public void add(final Schedule schedule) {
        schedules.add(schedule);
        schedules.sort(new LongTermComparator());
    }

    public List<Schedule> getSchedules() {
        return new ArrayList<>(schedules);
    }
}
