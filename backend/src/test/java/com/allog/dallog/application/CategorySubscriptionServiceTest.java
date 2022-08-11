package com.allog.dallog.application;

import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_이름;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.domain.member.application.MemberService;
import com.allog.dallog.domain.member.dto.MemberResponse;
import com.allog.dallog.domain.subscription.application.SubscriptionService;
import com.allog.dallog.domain.subscription.domain.Subscription;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class CategorySubscriptionServiceTest {

    @Autowired
    private CategorySubscriptionService categoryAndSubscriptionService;

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
        categoryAndSubscriptionService.save(파랑.getId(), 공통_일정_생성_요청);

        List<Subscription> subscriptions = subscriptionService.getAllByMemberId(파랑.getId());

        // then
        assertAll(() -> {
            assertThat(subscriptions).hasSize(1);
            assertThat(subscriptions.get(0).getCategory().getName()).isEqualTo(공통_일정_이름);
        });
    }
}
