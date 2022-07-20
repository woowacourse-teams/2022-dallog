package com.allog.dallog.subscription.dto.response;

import com.allog.dallog.category.dto.response.CategoryResponse;
import com.allog.dallog.subscription.domain.Subscription;

public class SubscriptionResponse {

    private Long id;
    private CategoryResponse category;
    private String color;

    private SubscriptionResponse() {
    }

    public SubscriptionResponse(final Subscription subscription) {
        this(subscription.getId(), new CategoryResponse(subscription.getCategory()), subscription.getColor());
    }

    public SubscriptionResponse(final Long id, final CategoryResponse category, final String color) {
        this.id = id;
        this.category = category;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public CategoryResponse getCategory() {
        return category;
    }

    public String getColor() {
        return color;
    }
}
