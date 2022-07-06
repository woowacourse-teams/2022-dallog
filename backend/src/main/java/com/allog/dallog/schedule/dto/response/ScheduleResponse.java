package com.allog.dallog.schedule.dto.response;

import com.allog.dallog.schedule.domain.Schedule;
import java.time.LocalDateTime;

public class ScheduleResponse {

    private final Long id;
    private final String title;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final String memo;

    public ScheduleResponse(final Schedule schedule) {
        this(schedule.getId(), schedule.getTitle(), schedule.getStartDateTime(),
            schedule.getEndDateTime(), schedule.getMemo());
    }

    public ScheduleResponse(final Long id, final String title, final LocalDateTime startDateTime,
        final LocalDateTime endDateTime, final String memo) {
        this.id = id;
        this.title = title;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.memo = memo;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public String getMemo() {
        return memo;
    }
}
