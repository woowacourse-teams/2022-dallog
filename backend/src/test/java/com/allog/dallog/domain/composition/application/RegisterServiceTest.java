package com.allog.dallog.domain.composition.application;

import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_OAUTH_MEMBER;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.domain.auth.dto.OAuthMember;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.category.exception.NoSuchCategoryException;
import com.allog.dallog.domain.member.application.MemberService;
import com.allog.dallog.domain.member.dto.MemberResponse;
import com.allog.dallog.domain.member.exception.NoSuchMemberException;
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
    private MemberService memberService;

    @Autowired
    private CategoryService categoryService;

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

    @DisplayName("유저 삭제 시 연관된 구독과 카테고리를 순차적으로 삭제한다.")
    @Test
    void 유저_삭제_시_연관된_구독과_카테고리를_순차적으로_삭제한다() {
        // given
        OAuthMember member = STUB_OAUTH_MEMBER();
        MemberResponse memberResponse = registerService.register(member);
        Long memberId = memberResponse.getId();

        CategoryResponse categoryResponse = categoryService.save(memberId, 공통_일정_생성_요청);
        subscriptionService.save(memberId, categoryResponse.getId());

        // when
        registerService.delete(memberId);

        // then
        assertAll(() -> {
            assertThatThrownBy(() -> memberService.getMember(memberId))
                    .isInstanceOf(NoSuchMemberException.class);
            assertThatThrownBy(() -> categoryService.getCategory(memberId))
                    .isInstanceOf(NoSuchCategoryException.class);
            assertThat(subscriptionService.getAllByMemberId(memberId)).hasSize(0);
        });
    }
}
