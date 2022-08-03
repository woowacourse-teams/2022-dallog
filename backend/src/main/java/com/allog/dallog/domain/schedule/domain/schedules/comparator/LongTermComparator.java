package com.allog.dallog.domain.schedule.domain.schedules.comparator;

import com.allog.dallog.domain.schedule.domain.Schedule;
import java.time.LocalDateTime;
import java.util.Comparator;

public class LongTermComparator implements Comparator<Schedule> {

    private static final int BEFORE = -1;
    private static final int AFTER = 1;
    private static final int SAME = 0;

    @Override
    public int compare(final Schedule firstSchedule, final Schedule secondSchedule) {
        LocalDateTime firstScheduleStartDateTime = firstSchedule.getStartDateTime();
        LocalDateTime secondScheduleStartDateTime = secondSchedule.getStartDateTime();

        if (firstScheduleStartDateTime.isBefore(secondScheduleStartDateTime)) {
            return BEFORE;
        }

        if (firstScheduleStartDateTime.isAfter(secondScheduleStartDateTime)) {
            return AFTER;
        }

        return SAME;
    }
}
