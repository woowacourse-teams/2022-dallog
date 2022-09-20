package com.allog.dallog.domain.schedule.domain;

import java.time.LocalDateTime;
import java.util.Comparator;

public class IntegrationScheduleComparator implements Comparator<IntegrationSchedule> {

    private static final int SAME_CONDITION = 0;

    @Override
    public int compare(final IntegrationSchedule firstSchedule, final IntegrationSchedule secondSchedule) {
        LocalDateTime firstScheduleStartDateTime = firstSchedule.getStartDateTime();
        LocalDateTime secondScheduleStartDateTime = secondSchedule.getStartDateTime();

        int cmp = firstScheduleStartDateTime.compareTo(secondScheduleStartDateTime);
        if (cmp == SAME_CONDITION) {
            return compareEndDateTime(firstSchedule, secondSchedule);
        }
        return cmp;
    }

    private int compareEndDateTime(IntegrationSchedule firstSchedule, IntegrationSchedule secondSchedule) {
        LocalDateTime firstScheduleEndDateTime = firstSchedule.getEndDateTime();
        LocalDateTime secondScheduleEndDateTime = secondSchedule.getEndDateTime();

        int cmp = secondScheduleEndDateTime.compareTo(firstScheduleEndDateTime);
        if (cmp == SAME_CONDITION) {
            return compareByTitle(firstSchedule, secondSchedule);
        }
        return cmp;
    }

    private int compareByTitle(IntegrationSchedule firstSchedule, IntegrationSchedule secondSchedule) {
        return firstSchedule.getTitle().compareTo(secondSchedule.getTitle());
    }
}
