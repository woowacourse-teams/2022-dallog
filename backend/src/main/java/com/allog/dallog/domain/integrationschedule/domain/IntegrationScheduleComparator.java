package com.allog.dallog.domain.integrationschedule.domain;

import java.time.LocalDateTime;
import java.util.Comparator;

public class IntegrationScheduleComparator implements Comparator<IntegrationSchedule> {

    private static final int BEFORE = -1;
    private static final int AFTER = 1;
    private static final int SAME = 0;

    @Override
    public int compare(final IntegrationSchedule firstSchedule, final IntegrationSchedule secondSchedule) {
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

    private int compareEndDateTime(IntegrationSchedule firstSchedule, IntegrationSchedule secondSchedule) {
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

    private int compareByTitle(IntegrationSchedule firstSchedule, IntegrationSchedule secondSchedule) {
        return firstSchedule.getTitle().compareTo(secondSchedule.getTitle());
    }
}
