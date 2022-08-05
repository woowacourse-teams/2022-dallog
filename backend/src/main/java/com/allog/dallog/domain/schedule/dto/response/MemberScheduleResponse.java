package com.allog.dallog.domain.schedule.dto.response;

import com.allog.dallog.domain.schedule.domain.Schedule;
import java.time.LocalDateTime;

public class MemberScheduleResponse {

    private final Long id;
    private final String title;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final String memo;
    private final Long categoryId;
    private final String color;

    public MemberScheduleResponse(final Schedule schedule, final String color) {
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.startDateTime = schedule.getStartDateTime();
        this.endDateTime = schedule.getEndDateTime();
        this.memo = schedule.getMemo();
        this.categoryId = schedule.getCategoryId();
        this.color = color;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public String getColor() {
        return color;
    }
}
