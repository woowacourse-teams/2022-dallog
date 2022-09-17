package com.allog.dallog.domain.subscription.domain;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.exception.NoSuchCategoryException;
import com.allog.dallog.domain.integrationschedule.domain.IntegrationSchedule;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Subscriptions {

    private final List<Subscription> subscriptions;

    public Subscriptions(final List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<Long> findCheckedCategoryIdsBy(final Predicate<Category> predicate) {
        return subscriptions.stream()
                .filter(Subscription::isChecked)
                .map(Subscription::getCategory)
                .filter(predicate)
                .map(Category::getId)
                .collect(Collectors.toList());
    }

    public Color findColor(final IntegrationSchedule schedule) {
        return subscriptions.stream()
                .filter(schedule::isSameCategory)
                .findAny()
                .orElseThrow(() -> new NoSuchCategoryException("구독하지 않은 카테고리 입니다."))
                .getColor();
    }
}
