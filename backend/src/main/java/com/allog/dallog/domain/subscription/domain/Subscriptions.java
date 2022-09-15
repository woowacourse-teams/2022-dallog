package com.allog.dallog.domain.subscription.domain;

import com.allog.dallog.domain.category.domain.Category;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Subscriptions {

    private final List<Subscription> subscriptions;

    public Subscriptions(final List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<Long> findCheckedCategoryIdsBy(Predicate<Category> predicate) {
        return subscriptions.stream()
                .filter(Subscription::isChecked)
                .map(Subscription::getCategory)
                .filter(predicate)
                .map(Category::getId)
                .collect(Collectors.toList());
    }
}
