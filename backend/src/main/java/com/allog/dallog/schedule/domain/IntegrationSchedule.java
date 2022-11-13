package com.allog.dallog.schedule.domain;

import com.allog.dallog.category.domain.CategoryType;
import com.allog.dallog.category.domain.Category;
import java.time.LocalDateTime;
import java.util.Objects;

public class IntegrationSchedule {

    private static final int ONE_DAY = 1;

    private final String id;
    private final Long categoryId;
    private final String title;
    private final Period period;
    private final String memo;
    private final CategoryType categoryType;

    public IntegrationSchedule(final Schedule schedule) {
        this.id = String.valueOf(schedule.getId());
        Category category = schedule.getCategory();
        this.categoryId = category.getId();
        this.title = schedule.getTitle();
        this.period = new Period(schedule.getStartDateTime(), schedule.getEndDateTime());
        this.memo = schedule.getMemo();
        this.categoryType = category.getCategoryType();
    }

    public IntegrationSchedule(final String id, final Long categoryId, final String title,
                               final LocalDateTime startDateTime, final LocalDateTime endDateTime, final String memo,
                               final CategoryType categoryType) {
        this(id, categoryId, title, new Period(startDateTime, endDateTime), memo, categoryType);
    }

    public IntegrationSchedule(final String id, final Long categoryId, final String title, final Period period,
                               final String memo, final CategoryType categoryType) {
        this.id = id;
        this.categoryId = categoryId;
        this.title = title;
        this.period = period;
        this.memo = memo;
        this.categoryType = categoryType;
    }

    public boolean isLongTerms() {
        return !isAllDays()
                && period.calculateDayDifference() >= ONE_DAY;
    }

    public boolean isAllDays() {
        return period.calculateDayDifference() == ONE_DAY
                && period.isMidnightToMidnight();
    }

    public boolean isFewHours() {
        return period.calculateDayDifference() < ONE_DAY;
    }

    public boolean isSameCategory(final Category category) {
        Long categoryId = category.getId();
        return this.categoryId.equals(categoryId);
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

    public LocalDateTime getStartDateTime() {
        return period.getStartDateTime();
    }

    public LocalDateTime getEndDateTime() {
        return period.getEndDateTime();
    }

    public Period getPeriod() {
        return period;
    }

    public String getMemo() {
        return memo;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IntegrationSchedule that = (IntegrationSchedule) o;
        return Objects.equals(id, that.id) && Objects.equals(categoryId, that.categoryId)
                && Objects.equals(title, that.title) && Objects.equals(period, that.period)
                && Objects.equals(memo, that.memo) && Objects.equals(categoryType, that.categoryType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categoryId, title, period, memo, categoryType);
    }
}
