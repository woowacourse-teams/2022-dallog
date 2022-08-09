package com.allog.dallog.domain.schedule.domain.repeat.strategy;

import com.allog.dallog.domain.schedule.domain.Schedule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EveryDayRepeatStrategy implements RepeatStrategy {

    private static final int REPEAT_DAY_UNIT = 1;

    @Override
    public List<Schedule> getSchedules(final Schedule initialSchedule, final LocalDate endDate) {
        List<Schedule> schedules = new ArrayList<>();
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);

        Schedule currentSchedule = initialSchedule;
        while (currentSchedule.getStartDateTime().isBefore(endDateTime)) {
            schedules.add(currentSchedule);
            currentSchedule = currentSchedule.plusDays(REPEAT_DAY_UNIT);
        }

        return schedules;
    }
}
