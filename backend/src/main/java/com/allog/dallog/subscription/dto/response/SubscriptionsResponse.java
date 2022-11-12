package com.allog.dallog.subscription.dto.response;

import java.util.List;

public class SubscriptionsResponse {

    private List<SubscriptionResponse> subscriptions;

    private SubscriptionsResponse() {
    }

    public SubscriptionsResponse(final List<SubscriptionResponse> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<SubscriptionResponse> getSubscriptions() {
        return subscriptions;
    }
}
