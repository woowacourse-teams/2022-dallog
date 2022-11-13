package com.allog.dallog.subscription.domain;

import com.allog.dallog.category.exception.NoSuchCategoryException;
import com.allog.dallog.category.domain.Category;
import com.allog.dallog.schedule.domain.IntegrationSchedule;
import java.util.List;
import java.util.stream.Collectors;

public class Subscriptions {

    private final List<Subscription> subscriptions;

    public Subscriptions(final List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<Category> findInternalCategory() {
        return subscriptions.stream()
                .filter(Subscription::isChecked)
                .filter(Subscription::hasInternalCategory)
                .map(Subscription::getCategory)
                .collect(Collectors.toList());
    }

    public List<Category> findExternalCategory() {
        return subscriptions.stream()
                .filter(Subscription::isChecked)
                .filter(Subscription::hasExternalCategory)
                .map(Subscription::getCategory)
                .collect(Collectors.toList());
    }

    public Color findColor(final IntegrationSchedule schedule) {
        return subscriptions.stream()
                .filter(subscription -> schedule.isSameCategory(subscription.getCategory()))
                .findAny()
                .orElseThrow(() -> new NoSuchCategoryException("구독하지 않은 카테고리 입니다."))
                .getColor();
    }
}
