package com.allog.dallog.schedule.dto.request;

import com.allog.dallog.schedule.domain.Schedule;
import java.time.LocalDateTime;

public class ScheduleCreateRequest {

    private String title;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String memo;

    public ScheduleCreateRequest(final String title, final LocalDateTime startDateTime,
        final LocalDateTime endDateTime, final String memo) {
        this.title = title;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.memo = memo;
    }

    public Schedule toEntity() {
        return new Schedule(title, startDateTime, endDateTime, memo);
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
