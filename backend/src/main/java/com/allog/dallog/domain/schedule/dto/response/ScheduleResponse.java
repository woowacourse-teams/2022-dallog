package com.allog.dallog.domain.schedule.dto.response;

import com.allog.dallog.domain.schedule.domain.Schedule;
import java.time.LocalDateTime;

public class ScheduleResponse {

    private final Long id;
    private final Long categoryId;
    private final String title;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final String memo;
    private final String categoryType;

    public ScheduleResponse(final Schedule schedule) {
        this(schedule.getId(), schedule.getCategory().getId(), schedule.getTitle(), schedule.getStartDateTime(),
                schedule.getEndDateTime(), schedule.getMemo(), schedule.getCategory().getCategoryType().name());
    }

    public ScheduleResponse(final Long id, final Long categoryId, final String title, final LocalDateTime startDateTime,
                            final LocalDateTime endDateTime, final String memo, final String categoryType) {
        this.id = id;
        this.categoryId = categoryId;
        this.title = title;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.memo = memo;
        this.categoryType = categoryType;
    }

    public Long getId() {
        return id;
    }

    public Long getCategoryId() {
        return categoryId;
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

    public String getCategoryType() {
        return categoryType;
    }
}
