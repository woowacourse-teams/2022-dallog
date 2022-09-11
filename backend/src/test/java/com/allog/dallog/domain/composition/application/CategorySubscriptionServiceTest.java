package com.allog.dallog.domain.composition.application;

import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_생성_요청;
import static com.allog.dallog.common.fixtures.ExternalCategoryFixtures.대한민국_공휴일_생성_요청;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.domain.member.application.MemberService;
import com.allog.dallog.domain.member.dto.MemberResponse;
import com.allog.dallog.domain.subscription.application.SubscriptionService;
import com.allog.dallog.domain.subscription.domain.Subscription;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CategorySubscriptionServiceTest extends ServiceTest {

    @Autowired
    private CategorySubscriptionService categorySubscriptionService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private SubscriptionService subscriptionService;

    @DisplayName("카테고리 생성 시 자동으로 구독한다.")
    @Test
    void 카테고리_생성_시_자동으로_구독한다() {
        // given
        MemberResponse 파랑 = memberService.save(파랑());

        // when
        categorySubscriptionService.save(파랑.getId(), 공통_일정_생성_요청);

        List<Subscription> subscriptions = subscriptionService.getAllByMemberId(파랑.getId());

        // then
        assertThat(subscriptions).hasSize(2);
    }

    @DisplayName("외부 카테고리 생성 시 자동으로 구독한다.")
    @Test
    void 외부_카테고리_생성_시_자동으로_구독한다() {
        // given
        MemberResponse 파랑 = memberService.save(파랑());

        // when
        categorySubscriptionService.save(파랑.getId(), 대한민국_공휴일_생성_요청);

        List<Subscription> subscriptions = subscriptionService.getAllByMemberId(파랑.getId());

        // then
        assertThat(subscriptions).hasSize(2);
    }
}
