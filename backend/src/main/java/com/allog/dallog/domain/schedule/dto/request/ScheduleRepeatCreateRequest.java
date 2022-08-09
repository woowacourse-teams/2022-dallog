package com.allog.dallog.domain.schedule.dto.request;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.schedule.domain.Schedule;
import java.time.LocalDate;

public class ScheduleRepeatCreateRequest {

    private final ScheduleCreateRequest schedule;
    private final String repeatType;
    private final LocalDate endDate;

    public ScheduleRepeatCreateRequest(final ScheduleCreateRequest schedule, final String repeatType,
                                       final LocalDate endDate) {
        this.schedule = schedule;
        this.repeatType = repeatType;
        this.endDate = endDate;
    }

    public Schedule toScheduleEntity(final Category category) {
        return schedule.toEntity(category);
    }

    public ScheduleCreateRequest getSchedule() {
        return schedule;
    }

    public String getRepeatType() {
        return repeatType;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
