package com.allog.dallog.common.fixtures;

import com.allog.dallog.subscription.dto.request.SubscriptionCreateRequest;

public class SubscriptionFixtures {

    public static final String COLOR_RED = "#FF0000";
    public static final String COLOR_BLUE = "#0000FF";
    public static final String COLOR_YELLOW = "#FFFF00";

    public static final SubscriptionCreateRequest CREATE_REQUEST_RED = new SubscriptionCreateRequest(COLOR_RED);
    public static final SubscriptionCreateRequest CREATE_REQUEST_BLUE = new SubscriptionCreateRequest(COLOR_BLUE);
    public static final SubscriptionCreateRequest CREATE_REQUEST_YELLOW = new SubscriptionCreateRequest(COLOR_YELLOW);
}
