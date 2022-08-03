package com.allog.dallog.domain.schedule.domain.schedules;

import com.allog.dallog.domain.schedule.domain.Schedule;
import java.time.LocalDateTime;
import java.util.Comparator;

public class ScheduleComparator implements Comparator<Schedule> {

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
        if (firstScheduleStartDateTime.isEqual(secondScheduleStartDateTime)) {
            return compareEndDateTime(firstSchedule, secondSchedule);
        }
        return SAME;
    }

    private int compareEndDateTime(Schedule firstSchedule, Schedule secondSchedule) {
        LocalDateTime firstScheduleEndDateTime = firstSchedule.getEndDateTime();
        LocalDateTime secondScheduleEndDateTime = secondSchedule.getEndDateTime();

        if (firstScheduleEndDateTime.isBefore(secondScheduleEndDateTime)) {
            return AFTER;
        }
        if (firstScheduleEndDateTime.isAfter(secondScheduleEndDateTime)) {
            return BEFORE;
        }
        return compareByTitle(firstSchedule, secondSchedule);
    }

    private int compareByTitle(Schedule firstSchedule, Schedule secondSchedule) {
        return firstSchedule.getTitle().compareTo(secondSchedule.getTitle());
    }
}
