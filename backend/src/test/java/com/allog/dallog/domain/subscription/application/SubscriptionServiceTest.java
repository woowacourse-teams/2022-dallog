package com.allog.dallog.domain.subscription.application;


import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_이름;
import static com.allog.dallog.common.fixtures.CategoryFixtures.FE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.내_일정_생성_요청;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.MemberFixtures.매트;
import static com.allog.dallog.common.fixtures.MemberFixtures.파랑;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.common.annotation.ServiceTest;
import com.allog.dallog.domain.auth.exception.NoPermissionException;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.member.application.MemberService;
import com.allog.dallog.domain.member.dto.MemberResponse;
import com.allog.dallog.domain.subscription.domain.Color;
import com.allog.dallog.domain.subscription.dto.request.SubscriptionUpdateRequest;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionResponse;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionsResponse;
import com.allog.dallog.domain.subscription.exception.ExistSubscriptionException;
import com.allog.dallog.domain.subscription.exception.InvalidSubscriptionException;
import com.allog.dallog.domain.subscription.exception.NoSuchSubscriptionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class SubscriptionServiceTest extends ServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubscriptionService subscriptionService;

    @DisplayName("새로운 구독을 생성한다.")
    @Test
    void 새로운_구독을_생성한다() {
        // given
        MemberResponse 후디 = memberService.save(후디());
        CategoryResponse BE_일정 = categoryService.save(후디.getId(), BE_일정_생성_요청);

        // when
        SubscriptionResponse response = subscriptionService.save(후디.getId(), BE_일정.getId());

        // then
        assertThat(response.getCategory().getName()).isEqualTo(BE_일정_이름);
    }

    @DisplayName("자신이 생성하지 않은 개인 카테고리를 구독시 예외가 발생한다.")
    @Test
    void 자신이_생성하지_않은_개인_카테고리를_구독시_예외가_발생한다() {
        // given
        MemberResponse 후디 = memberService.save(후디());
        CategoryResponse 후디_개인_학습_일정 = categoryService.save(후디.getId(), 내_일정_생성_요청);

        MemberResponse 매트 = memberService.save(매트());

        // when & then
        assertThatThrownBy(() -> subscriptionService.save(매트.getId(), 후디_개인_학습_일정.getId()))
                .isInstanceOf(NoPermissionException.class)
                .hasMessage("구독 권한이 없는 카테고리입니다.");
    }

    @DisplayName("이미 존재하는 구독 정보를 저장할 경우 예외를 던진다.")
    @Test
    void 이미_존재하는_구독_정보를_저장할_경우_예외를_던진다() {
        // given
        MemberResponse 후디 = memberService.save(후디());
        CategoryResponse BE_일정 = categoryService.save(후디.getId(), BE_일정_생성_요청);
        subscriptionService.save(후디.getId(), BE_일정.getId());

        // when & then
        assertThatThrownBy(() -> subscriptionService.save(후디.getId(), BE_일정.getId()))
                .isInstanceOf(ExistSubscriptionException.class);
    }

    @DisplayName("구독 id를 기반으로 단건 조회한다.")
    @Test
    void 구독_id를_기반으로_단건_조회한다() {
        // given
        MemberResponse 후디 = memberService.save(후디());
        CategoryResponse BE_일정 = categoryService.save(후디.getId(), BE_일정_생성_요청);
        SubscriptionResponse 빨간색_구독 = subscriptionService.save(후디.getId(), BE_일정.getId());

        // when
        SubscriptionResponse foundResponse = subscriptionService.findById(빨간색_구독.getId());

        // then
        assertAll(() -> {
            assertThat(foundResponse.getId()).isEqualTo(빨간색_구독.getId());
            assertThat(foundResponse.getCategory().getId()).isEqualTo(BE_일정.getId());
        });
    }

    @DisplayName("존재하지 않는 구독 정보인 경우 예외를 던진다.")
    @Test
    void 존재하지_않는_구독_정보인_경우_예외를_던진다() {
        // given & when & then
        assertThatThrownBy(() -> subscriptionService.findById(0L))
                .isInstanceOf(NoSuchSubscriptionException.class);
    }

    @DisplayName("회원 정보를 기반으로 구독 정보를 조회한다.")
    @Test
    void 회원_정보를_기반으로_구독_정보를_조회한다() {
        // given
        MemberResponse 관리자 = memberService.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);
        CategoryResponse BE_일정 = categoryService.save(관리자.getId(), BE_일정_생성_요청);
        CategoryResponse FE_일정 = categoryService.save(관리자.getId(), FE_일정_생성_요청);

        MemberResponse 후디 = memberService.save(후디());
        subscriptionService.save(후디.getId(), 공통_일정.getId());
        subscriptionService.save(후디.getId(), BE_일정.getId());
        subscriptionService.save(후디.getId(), FE_일정.getId());

        // when
        SubscriptionsResponse subscriptionsResponse = subscriptionService.findByMemberId(후디.getId());

        // then
        assertThat(subscriptionsResponse.getSubscriptions()).hasSize(3);
    }

    @DisplayName("구독 정보를 수정한다.")
    @Test
    void 구독_정보를_수정한다() {
        // given
        MemberResponse 후디 = memberService.save(후디());
        CategoryResponse BE_일정 = categoryService.save(후디.getId(), BE_일정_생성_요청);
        SubscriptionResponse response = subscriptionService.save(후디.getId(), BE_일정.getId());
        Color color = Color.COLOR_1;

        // when
        SubscriptionUpdateRequest request = new SubscriptionUpdateRequest(color, true);
        subscriptionService.update(response.getId(), 후디.getId(), request);

        // then
        assertAll(() -> {
            assertThat(request.getColor()).isEqualTo(color);
            assertThat(request.isChecked()).isTrue();
        });
    }

    @DisplayName("구독 정보 수정 시 존재하지 않는 색상인 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"#111", "#1111", "#11111", "123456", "#**1234", "##12345", "334172#", "#00FF00"})
    void 구독_정보_수정_시_존재하지_않는_색상인_경우_예외를_던진다(final String colorCode) {
        // given
        MemberResponse 후디 = memberService.save(후디());
        CategoryResponse BE_일정 = categoryService.save(후디.getId(), BE_일정_생성_요청);
        SubscriptionResponse response = subscriptionService.save(후디.getId(), BE_일정.getId());

        // when
        SubscriptionUpdateRequest request = new SubscriptionUpdateRequest(colorCode, true);

        // then
        assertThatThrownBy(() -> subscriptionService.update(response.getId(), 후디.getId(), request))
                .isInstanceOf(InvalidSubscriptionException.class);
    }

    @DisplayName("구독 정보를 삭제한다.")
    @Test
    void 구독_정보를_삭제한다() {
        // given
        MemberResponse 관리자 = memberService.save(관리자());
        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);
        CategoryResponse BE_일정 = categoryService.save(관리자.getId(), BE_일정_생성_요청);
        CategoryResponse FE_일정 = categoryService.save(관리자.getId(), FE_일정_생성_요청);

        MemberResponse 후디 = memberService.save(후디());
        SubscriptionResponse response = subscriptionService.save(후디.getId(), 공통_일정.getId());
        subscriptionService.save(후디.getId(), BE_일정.getId());
        subscriptionService.save(후디.getId(), FE_일정.getId());

        // when
        subscriptionService.deleteById(response.getId(), 후디.getId());

        // then
        assertThat(subscriptionService.findByMemberId(후디.getId()).getSubscriptions()).hasSize(2);
    }

    @DisplayName("자신의 구독 정보가 아닌 구독을 삭제할 경우 예외를 던진다.")
    @Test
    void 자신의_구독_정보가_아닌_구독을_삭제할_경우_예외를_던진다() {
        // given
        MemberResponse 관리자 = memberService.save(관리자());
        MemberResponse 파랑 = memberService.save(파랑());

        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);
        SubscriptionResponse 공통_일정_구독 = subscriptionService.save(파랑.getId(), 공통_일정.getId());

        // when & then
        assertThatThrownBy(() -> subscriptionService.deleteById(공통_일정_구독.getId(), 관리자.getId()))
                .isInstanceOf(NoPermissionException.class);
    }

    @DisplayName("자신이 만든 카테고리에 대한 구독을 삭제할 경우 예외를 던진다")
    @Test
    void 자신이_만든_카테고리에_대한_구독을_삭제할_경우_예외를_던진다() {
        // given
        MemberResponse 관리자 = memberService.save(관리자());

        CategoryResponse 공통_일정 = categoryService.save(관리자.getId(), 공통_일정_생성_요청);
        SubscriptionResponse 공통_일정_구독 = subscriptionService.save(관리자.getId(), 공통_일정.getId());

        // when & then
        assertThatThrownBy(() -> subscriptionService.deleteById(공통_일정_구독.getId(), 관리자.getId()))
                .isInstanceOf(NoPermissionException.class);
    }
}
