package com.allog.dallog.domain.integrationschedule.domain;

import com.allog.dallog.domain.schedule.domain.Period;

public class IntegrationSchedule {

    private final String id;
    private final Long categoryId;
    private final String title;
    private final Period period;
    private final String memo;

    public IntegrationSchedule(final String id, final Long categoryId, final String title, final Period period,
                               final String memo) {
        this.id = id;
        this.categoryId = categoryId;
        this.title = title;
        this.period = period;
        this.memo = memo;
    }

    public String getId() {
        return id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getTitle() {
        return title;
    }

    public Period getPeriod() {
        return period;
    }

    public String getMemo() {
        return memo;
    }
}
