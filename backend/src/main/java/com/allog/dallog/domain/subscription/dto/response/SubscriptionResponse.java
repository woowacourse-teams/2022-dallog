package com.allog.dallog.domain.subscription.dto.response;

import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.subscription.domain.Subscription;

public class SubscriptionResponse {

    private Long id;
    private CategoryResponse category;
    private String color;

    private boolean checked;

    private SubscriptionResponse() {
    }

    public SubscriptionResponse(final Subscription subscription) {
        this(subscription.getId(), new CategoryResponse(subscription.getCategory()), subscription.getColor(),
                subscription.isChecked());
    }

    public SubscriptionResponse(final Long id, final CategoryResponse category, final String color,
                                final boolean checked) {
        this.id = id;
        this.category = category;
        this.color = color;
        this.checked = checked;
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

    public boolean isChecked() {
        return checked;
    }
}
