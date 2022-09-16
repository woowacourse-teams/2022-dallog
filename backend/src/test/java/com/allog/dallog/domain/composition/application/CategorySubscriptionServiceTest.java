package com.allog.dallog.domain.composition.application;

import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_생성_요청;
import static com.allog.dallog.common.fixtures.ExternalCategoryFixtures.대한민국_공휴일_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.common.fixtures.AuthFixtures;
import com.allog.dallog.domain.subscription.domain.Subscription;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CategorySubscriptionServiceTest extends ServiceTest {

    @Autowired
    private CategorySubscriptionService categorySubscriptionService;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @DisplayName("카테고리 생성 시 자동으로 구독한다.")
    @Test
    void 카테고리_생성_시_자동으로_구독한다() {
        // given
        Long 파랑_id = parseMemberId(AuthFixtures.파랑_인증_코드_토큰_요청());

        // when
        categorySubscriptionService.save(파랑_id, 공통_일정_생성_요청);

        List<Subscription> subscriptions = subscriptionRepository.findByMemberId(파랑_id);

        // then
        assertThat(subscriptions).hasSize(2);
    }

    @DisplayName("외부 카테고리 생성 시 자동으로 구독한다.")
    @Test
    void 외부_카테고리_생성_시_자동으로_구독한다() {
        // given
        Long 파랑_id = parseMemberId(AuthFixtures.파랑_인증_코드_토큰_요청());

        // when
        categorySubscriptionService.save(파랑_id, 대한민국_공휴일_생성_요청);

        List<Subscription> subscriptions = subscriptionRepository.findByMemberId(파랑_id);

        // then
        assertThat(subscriptions).hasSize(2);
    }
}
