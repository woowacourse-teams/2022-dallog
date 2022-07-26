package com.allog.dallog.domain.subscription.application;


import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정_이름;
import static com.allog.dallog.common.fixtures.CategoryFixtures.FE_일정_생성_요청;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_생성_요청;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.노란색_구독_생성_요청;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.빨간색;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.빨간색_구독_생성_요청;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.파란색_구독_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.domain.auth.exception.NoPermissionException;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.member.application.MemberService;
import com.allog.dallog.domain.member.dto.MemberResponse;
import com.allog.dallog.domain.subscription.dto.request.SubscriptionCreateRequest;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionResponse;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionsResponse;
import com.allog.dallog.domain.subscription.exception.InvalidSubscriptionException;
import com.allog.dallog.domain.subscription.exception.NoSuchSubscriptionException;
import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
class SubscriptionServiceTest {

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
        SubscriptionResponse response = subscriptionService.save(후디.getId(), BE_일정.getId(), 빨간색_구독_생성_요청);

        // then
        assertAll(() -> {
            assertThat(response.getCategory().getName()).isEqualTo(BE_일정_이름);
            assertThat(response.getColor()).isEqualTo(빨간색);
        });
    }

    @DisplayName("색 정보 형식이 잘못된 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"#111", "#1111", "#11111", "123456", "#**1234", "##12345", "334172#"})
    void 색_정보_형식이_잘못된_경우_예외를_던진다(final String color) {
        // given
        MemberResponse 후디 = memberService.save(후디());
        CategoryResponse categoryResponse = categoryService.save(후디.getId(), BE_일정_생성_요청);

        // when & then
        assertThatThrownBy(() -> subscriptionService.save(후디.getId(), categoryResponse.getId(),
                new SubscriptionCreateRequest(color))).isInstanceOf(InvalidSubscriptionException.class);
    }

    @DisplayName("구독 id를 기반으로 단건 조회한다.")
    @Test
    void 구독_id를_기반으로_단건_조회한다() {
        // given
        MemberResponse 후디 = memberService.save(후디());
        CategoryResponse BE_일정 = categoryService.save(후디.getId(), BE_일정_생성_요청);
        SubscriptionResponse 빨간색_구독 = subscriptionService.save(후디.getId(), BE_일정.getId(), 빨간색_구독_생성_요청);

        // when
        SubscriptionResponse foundResponse = subscriptionService.findById(빨간색_구독.getId());

        // then
        assertAll(() -> {
            assertThat(foundResponse.getId()).isEqualTo(빨간색_구독.getId());
            assertThat(foundResponse.getCategory().getId()).isEqualTo(BE_일정.getId());
            assertThat(foundResponse.getColor()).isEqualTo(빨간색);
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
        subscriptionService.save(후디.getId(), 공통_일정.getId(), 빨간색_구독_생성_요청);
        subscriptionService.save(후디.getId(), BE_일정.getId(), 파란색_구독_생성_요청);
        subscriptionService.save(후디.getId(), FE_일정.getId(), 노란색_구독_생성_요청);

        // when
        SubscriptionsResponse subscriptionsResponse = subscriptionService.findByMemberId(후디.getId());

        // then
        assertThat(subscriptionsResponse.getSubscriptions()).hasSize(3);
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
        SubscriptionResponse subscriptionResponse = subscriptionService.save(후디.getId(), 공통_일정.getId(),
                빨간색_구독_생성_요청);
        subscriptionService.save(후디.getId(), BE_일정.getId(), 파란색_구독_생성_요청);
        subscriptionService.save(후디.getId(), FE_일정.getId(), 노란색_구독_생성_요청);

        // when
        subscriptionService.deleteByIdAndMemberId(subscriptionResponse.getId(), 후디.getId());

        // then
        assertThat(subscriptionService.findByMemberId(후디.getId()).getSubscriptions()).hasSize(2);
    }

    @DisplayName("자신의 구독 정보가 아닌 구독을 삭제할 경우 예외를 던진다.")
    @Test
    void 자신의_구독_정보가_아닌_구독을_삭제할_경우_예외를_던진다() {
        // given
        MemberResponse 관리자 = memberService.save(관리자());

        // when & then
        assertThatThrownBy(() -> subscriptionService.deleteByIdAndMemberId(0L, 관리자.getId()))
                .isInstanceOf(NoPermissionException.class);
    }
}
