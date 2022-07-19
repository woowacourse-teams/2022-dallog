package com.allog.dallog.subscription.dto.request;

public class SubscriptionCreateRequest {

    private String color;

    private SubscriptionCreateRequest() {
    }

    public SubscriptionCreateRequest(final String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
