package com.allog.dallog.common.fixtures;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.subscription.domain.Color;
import com.allog.dallog.domain.subscription.domain.Subscription;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionResponse;

public class SubscriptionFixtures {

    public static Subscription 색상1_구독(final Member member, final Category category) {
        return new Subscription(member, category, Color.COLOR_1);
    }

    public static SubscriptionResponse 색상1_구독_응답(final CategoryResponse categoryResponse) {
        return new SubscriptionResponse(1L, categoryResponse, Color.COLOR_1, true);
    }

    public static Subscription 색상2_구독(final Member member, final Category category) {
        return new Subscription(member, category, Color.COLOR_2);
    }

    public static SubscriptionResponse 색상2_구독_응답(final CategoryResponse categoryResponse) {
        return new SubscriptionResponse(2L, categoryResponse, Color.COLOR_2, true);
    }

    public static Subscription 색상3_구독(final Member member, final Category category) {
        return new Subscription(member, category, Color.COLOR_3);
    }

    public static SubscriptionResponse 색상3_구독_응답(final CategoryResponse categoryResponse) {
        return new SubscriptionResponse(3L, categoryResponse, Color.COLOR_3, true);
    }
}
