package com.allog.dallog.common.fixtures;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.subscription.domain.Subscription;
import com.allog.dallog.domain.subscription.dto.request.SubscriptionCreateRequest;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionResponse;

public class SubscriptionFixtures {

    /* 빨간색 구독 */
    public static final String 빨간색 = "#FF0000";
    public static final SubscriptionCreateRequest 빨간색_구독_생성_요청 = new SubscriptionCreateRequest(빨간색);

    /* 파란색 구독 */
    public static final String 파란색 = "#0000FF";
    public static final SubscriptionCreateRequest 파란색_구독_생성_요청 = new SubscriptionCreateRequest(파란색);

    /* 노란색 구독 */
    public static final String 노란색 = "#FFFF00";
    public static final SubscriptionCreateRequest 노란색_구독_생성_요청 = new SubscriptionCreateRequest(노란색);

    public static Subscription 빨간색_구독(final Member member, final Category category) {
        return new Subscription(member, category, 빨간색);
    }

    public static SubscriptionResponse 빨간색_구독_응답(final CategoryResponse categoryResponse) {
        return new SubscriptionResponse(1L, categoryResponse, 빨간색);
    }

    public static Subscription 파란색_구독(final Member member, final Category category) {
        return new Subscription(member, category, 파란색);
    }

    public static SubscriptionResponse 파란색_구독_응답(final CategoryResponse categoryResponse) {
        return new SubscriptionResponse(2L, categoryResponse, 파란색);
    }

    public static Subscription 노란색_구독(final Member member, final Category category) {
        return new Subscription(member, category, 노란색);
    }

    public static SubscriptionResponse 노란색_구독_응답(final CategoryResponse categoryResponse) {
        return new SubscriptionResponse(3L, categoryResponse, 노란색);
    }
}
