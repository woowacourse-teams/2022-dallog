package com.allog.dallog.subscription.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.category.dto.response.CategoryResponse;
import com.allog.dallog.category.service.CategoryService;
import com.allog.dallog.common.fixtures.MemberFixtures;
import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.service.MemberService;
import com.allog.dallog.subscription.dto.request.SubscriptionCreateRequest;
import com.allog.dallog.subscription.dto.response.SubscriptionResponse;
import com.allog.dallog.subscription.exception.InvalidSubscriptionException;
import com.allog.dallog.subscription.exception.NoSuchSubscriptionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
        Member member = memberService.save(MemberFixtures.MEMBER);
        CategoryResponse categoryResponse = categoryService.save(new CategoryCreateRequest("BE 일정"));
        String color = "#ffffff";

        // when
        Long id = subscriptionService.save(member.getId(), categoryResponse.getId(),
                new SubscriptionCreateRequest(color));

        // then
        assertThat(id).isNotNull();
    }

    @DisplayName("색 정보 형식이 잘못된 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"#111", "#1111", "#11111", "123456", "#**1234", "##12345", "334172#"})
    void 색_정보_형식이_잘못된_경우_예외를_던진다(final String color) {
        // given
        Member member = memberService.save(MemberFixtures.MEMBER);
        CategoryResponse categoryResponse = categoryService.save(new CategoryCreateRequest("BE 일정"));

        // when & then
        assertThatThrownBy(() -> subscriptionService.save(member.getId(), categoryResponse.getId(),
                new SubscriptionCreateRequest(color))).isInstanceOf(InvalidSubscriptionException.class);
    }

    @DisplayName("구독 id를 기반으로 단건 조회한다.")
    @Test
    void 구독_id를_기반으로_단건_조회한다() {
        // given
        Member member = memberService.save(MemberFixtures.MEMBER);
        CategoryResponse categoryResponse = categoryService.save(new CategoryCreateRequest("BE 일정"));
        String color = "#ffffff";
        Long id = subscriptionService.save(member.getId(), categoryResponse.getId(),
                new SubscriptionCreateRequest(color));

        // when
        SubscriptionResponse subscriptionResponse = subscriptionService.findById(id);

        // then
        assertAll(() -> {
            assertThat(subscriptionResponse.getId()).isEqualTo(id);
            assertThat(subscriptionResponse.getCategory().getId()).isEqualTo(categoryResponse.getId());
            assertThat(subscriptionResponse.getColor()).isEqualTo(color);
        });
    }

    @DisplayName("존재하지 않는 구독 정보인 경우 예외를 던진다.")
    @Test
    void 존재하지_않는_구독_정보인_경우_예외를_던진다() {
        // given & when & then
        assertThatThrownBy(() -> subscriptionService.findById(0L))
                .isInstanceOf(NoSuchSubscriptionException.class);
    }
}
