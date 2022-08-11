package com.allog.dallog.domain.composition.application;

import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_OAUTH_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.domain.auth.dto.OAuthMember;
import com.allog.dallog.domain.member.dto.MemberResponse;
import com.allog.dallog.domain.subscription.application.SubscriptionService;
import com.allog.dallog.domain.subscription.domain.Subscription;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RegisterServiceTest extends ServiceTest {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private SubscriptionService subscriptionService;

    @DisplayName("유저 생성 시 개인 카테고리를 자동으로 생성하고 구독한다.")
    @Test
    void 유저_생성_시_개인_카테고리를_자동으로_생성하고_구독한다() {
        // given & when
        OAuthMember member = STUB_OAUTH_MEMBER();
        MemberResponse memberResponse = registerService.register(member);

        List<Subscription> subscriptions = subscriptionService.getAllByMemberId(memberResponse.getId());

        // then
        assertAll(() -> {
            assertThat(memberResponse.getEmail()).isEqualTo(member.getEmail());
            assertThat(subscriptions).hasSize(1);
        });
    }
}
