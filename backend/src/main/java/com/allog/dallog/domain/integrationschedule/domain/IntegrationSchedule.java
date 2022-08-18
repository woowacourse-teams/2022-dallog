package com.allog.dallog.domain.integrationschedule.domain;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.exception.NoSuchCategoryException;
import com.allog.dallog.domain.subscription.domain.Color;
import com.allog.dallog.domain.subscription.domain.Subscription;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class IntegrationSchedule {

    private static final int ONE_DAY = 1;
    private static final int MIDNIGHT_HOUR = 23;
    private static final int MIDNIGHT_MINUTE = 59;

    private final String id;
    private final Long categoryId;
    private final String title;
    private final Period period;
    private final String memo;
    private final String categoryType;

    public IntegrationSchedule(final String id, final Long categoryId, final String title,
                               final LocalDateTime startDateTime, final LocalDateTime endDateTime, final String memo,
                               final String categoryType) {
        this(id, categoryId, title, new Period(startDateTime, endDateTime), memo, categoryType);
    }

    public IntegrationSchedule(final String id, final Long categoryId, final String title, final Period period,
                               final String memo, final String categoryType) {
        this.id = id;
        this.categoryId = categoryId;
        this.title = title;
        this.period = period;
        this.memo = memo;
        this.categoryType = categoryType;
    }

    public boolean isLongTerms() {
        return period.calculateDayDifference() >= ONE_DAY;
    }

    public boolean isAllDays() {
        return period.calculateDayDifference() < ONE_DAY
                && period.calculateHourDifference() == MIDNIGHT_HOUR
                && period.calculateMinuteDifference() == MIDNIGHT_MINUTE;
    }

    public boolean isFewHours() {
        return !isAllDays()
                && period.calculateDayDifference() < ONE_DAY;
    }

    public Color findSubscriptionColor(final List<Subscription> subscriptions) {
        return subscriptions.stream()
                .filter(this::isSameCategory)
                .findAny()
                .orElseThrow(() -> new NoSuchCategoryException("구독하지 않은 카테고리 입니다."))
                .getColor();
    }

    private boolean isSameCategory(final Subscription subscription) {
        Category category = subscription.getCategory();
        return category.getId().equals(categoryId);
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

    public String getCategoryType() {
        return categoryType;
    }

    // 중복 일정을 판별하기 위한 equals & hashCode

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
