package com.allog.dallog.subscription.service;


import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.DISPLAY_NAME;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.EMAIL;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.PROFILE_IMAGE_URI;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.COLOR_BLUE;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.COLOR_RED;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.COLOR_YELLOW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.auth.exception.NoPermissionException;
import com.allog.dallog.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.category.dto.response.CategoryResponse;
import com.allog.dallog.category.service.CategoryService;
import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.SocialType;
import com.allog.dallog.member.dto.MemberResponse;
import com.allog.dallog.member.service.MemberService;
import com.allog.dallog.subscription.dto.request.SubscriptionCreateRequest;
import com.allog.dallog.subscription.dto.response.SubscriptionResponse;
import com.allog.dallog.subscription.dto.response.SubscriptionsResponse;
import com.allog.dallog.subscription.exception.InvalidSubscriptionException;
import com.allog.dallog.subscription.exception.NoSuchSubscriptionException;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
class SubscriptionServiceTest {

    private final CategoryCreateRequest categoryCreateRequest = new CategoryCreateRequest("BE 일정");

    @Autowired
    private MemberService memberService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubscriptionService subscriptionService;

    private MemberResponse memberResponse;
    private CategoryResponse firstCategoryResponse;
    private CategoryResponse secondCategoryResponse;
    private CategoryResponse thirdCategoryResponse;

    @BeforeEach
    void setUp() {
        memberResponse = memberService.save(new Member(EMAIL, PROFILE_IMAGE_URI, DISPLAY_NAME, SocialType.GOOGLE));
        firstCategoryResponse = categoryService.save(memberResponse.getId(), categoryCreateRequest);
        secondCategoryResponse = categoryService.save(memberResponse.getId(), categoryCreateRequest);
        thirdCategoryResponse = categoryService.save(memberResponse.getId(), categoryCreateRequest);
    }

    @DisplayName("새로운 구독을 생성한다.")
    @Test
    void 새로운_구독을_생성한다() {
        // given & when
        SubscriptionResponse response = subscriptionService.save(memberResponse.getId(), firstCategoryResponse.getId(),
                new SubscriptionCreateRequest(COLOR_RED));

        // then
        assertAll(() -> {
            assertThat(response.getCategory().getName()).isEqualTo("BE 일정");
            assertThat(response.getColor()).isEqualTo(COLOR_RED);
        });
    }

    @DisplayName("색 정보 형식이 잘못된 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"#111", "#1111", "#11111", "123456", "#**1234", "##12345", "334172#"})
    void 색_정보_형식이_잘못된_경우_예외를_던진다(final String color) {
        // given & when & then
        assertThatThrownBy(() -> subscriptionService.save(memberResponse.getId(), firstCategoryResponse.getId(),
                new SubscriptionCreateRequest(color))).isInstanceOf(InvalidSubscriptionException.class);
    }

    @DisplayName("구독 id를 기반으로 단건 조회한다.")
    @Test
    void 구독_id를_기반으로_단건_조회한다() {
        // given
        SubscriptionResponse subscriptionResponse = subscriptionService.save(memberResponse.getId(),
                firstCategoryResponse.getId(), new SubscriptionCreateRequest(COLOR_RED));

        // when
        SubscriptionResponse foundResponse = subscriptionService.findById(subscriptionResponse.getId());

        // then
        assertAll(() -> {
            assertThat(foundResponse.getId()).isEqualTo(subscriptionResponse.getId());
            assertThat(foundResponse.getCategory().getId()).isEqualTo(firstCategoryResponse.getId());
            assertThat(foundResponse.getColor()).isEqualTo(COLOR_RED);
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
        Long memberId = memberResponse.getId();
        subscriptionService.save(memberId, firstCategoryResponse.getId(), new SubscriptionCreateRequest(COLOR_RED));
        subscriptionService.save(memberId, secondCategoryResponse.getId(), new SubscriptionCreateRequest(COLOR_BLUE));
        subscriptionService.save(memberId, thirdCategoryResponse.getId(), new SubscriptionCreateRequest(COLOR_YELLOW));

        // when
        SubscriptionsResponse subscriptionsResponse = subscriptionService.findByMemberId(memberId);

        // then
        assertThat(subscriptionsResponse.getSubscriptions()).hasSize(3);
    }

    @DisplayName("구독 정보를 삭제한다.")
    @Test
    void 구독_정보를_삭제한다() {
        // given
        Long memberId = memberResponse.getId();
        SubscriptionResponse subscriptionResponse = subscriptionService.save(memberId, firstCategoryResponse.getId(),
                new SubscriptionCreateRequest(COLOR_RED));
        subscriptionService.save(memberId, secondCategoryResponse.getId(), new SubscriptionCreateRequest(COLOR_BLUE));
        subscriptionService.save(memberId, thirdCategoryResponse.getId(), new SubscriptionCreateRequest(COLOR_YELLOW));

        // when
        subscriptionService.deleteByIdAndMemberId(subscriptionResponse.getId(), memberId);

        // then
        assertThat(subscriptionService.findByMemberId(memberId).getSubscriptions()).hasSize(2);
    }

    @DisplayName("자신의 구독 정보가 아닌 구독을 삭제할 경우 예외를 던진다.")
    @Test
    void 자신의_구독_정보가_아닌_구독을_삭제할_경우_예외를_던진다() {
        // given
        Long memberId = memberResponse.getId();

        // when & then
        assertThatThrownBy(() -> subscriptionService.deleteByIdAndMemberId(0L, memberId))
                .isInstanceOf(NoPermissionException.class);
    }
}
