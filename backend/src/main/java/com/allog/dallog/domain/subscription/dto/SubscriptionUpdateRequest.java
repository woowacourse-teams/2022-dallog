package com.allog.dallog.domain.subscription.dto;

public class SubscriptionUpdateRequest {

    private String color;
    private boolean checked;

    private SubscriptionUpdateRequest() {
    }

    public SubscriptionUpdateRequest(final String color, final boolean checked) {
        this.color = color;
        this.checked = checked;
    }

    public String getColor() {
        return color;
    }

    public boolean isChecked() {
        return checked;
    }
}
