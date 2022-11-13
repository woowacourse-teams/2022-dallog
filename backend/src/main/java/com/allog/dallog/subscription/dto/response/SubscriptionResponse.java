package com.allog.dallog.subscription.dto.response;

import com.allog.dallog.category.dto.response.CategoryResponse;
import com.allog.dallog.subscription.domain.Color;
import com.allog.dallog.subscription.domain.Subscription;

public class SubscriptionResponse {

    private Long id;
    private CategoryResponse category;
    private String colorCode;

    private boolean checked;

    private SubscriptionResponse() {
    }

    public SubscriptionResponse(final Subscription subscription) {
        this(subscription.getId(), new CategoryResponse(subscription.getCategory()), subscription.getColor(),
                subscription.isChecked());
    }

    public SubscriptionResponse(final Long id, final CategoryResponse category, final Color color,
                                final boolean checked) {
        this.id = id;
        this.category = category;
        this.colorCode = color.getColorCode();
        this.checked = checked;
    }

    public Long getId() {
        return id;
    }

    public CategoryResponse getCategory() {
        return category;
    }

    public String getColorCode() {
        return colorCode;
    }

    public boolean isChecked() {
        return checked;
    }
}
