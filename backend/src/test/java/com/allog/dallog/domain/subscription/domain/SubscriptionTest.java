package com.allog.dallog.domain.subscription.domain;

import static com.allog.dallog.common.fixtures.CategoryFixtures.매트_아고라;
import static com.allog.dallog.common.fixtures.CategoryFixtures.후디_JPA_스터디;
import static com.allog.dallog.common.fixtures.MemberFixtures.매트;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.빨간색_구독;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.subscription.exception.InvalidSubscriptionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SubscriptionTest {

    @DisplayName("구독을 생성한다.")
    @Test
    void 구독을_생성한다() {
        // given
        Member 후디 = 후디();
        Category 후디_JPA_스터디 = 후디_JPA_스터디(후디);
        String color = "#c9ad2e";

        // when & then
        assertDoesNotThrow(() -> new Subscription(후디, 후디_JPA_스터디, color));
    }

    @DisplayName("색 정보 형식이 잘못된 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"#111", "#1111", "#11111", "123456", "#**1234", "##12345", "334172#"})
    void 색_정보_형식이_잘못된_경우_예외를_던진다(final String color) {
        // given
        Member 후디 = 후디();
        Category 후디_JPA_스터디 = 후디_JPA_스터디(후디);

        // when & then
        assertThatThrownBy(() -> new Subscription(후디, 후디_JPA_스터디, color))
                .isInstanceOf(InvalidSubscriptionException.class);
    }

    @DisplayName("구독이 생성되면 기본적으로 체크된다.")
    @Test
    void 구독이_생성되면_기본적으로_체크된다() {
        // given
        Member 매트 = 매트();
        Category 매트_아고라 = 매트_아고라(매트);
        String color = "#c9ad2e";

        // when
        Subscription actual = new Subscription(매트, 매트_아고라, color);

        // then
        assertThat(actual.isChecked()).isTrue();
    }

    @DisplayName("구독의 색 정보를 수정한다.")
    @Test
    void 구독의_색_정보를_수정한다() {
        // given
        Member 매트 = 매트();
        Category 매트_아고라 = 매트_아고라(매트);

        // when 
        Subscription actual = 빨간색_구독(매트, 매트_아고라);
        actual.change("#c9ad2e", actual.isChecked());

        // then
        assertThat(actual.getColor()).isEqualTo("#c9ad2e");
    }

    @DisplayName("구독 수정 시 색 정보 형식이 잘못된 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"#111", "#1111", "#11111", "123456", "#**1234", "##12345", "334172#"})
    void 구독_수정_시_색_정보_형식이_잘못된_경우_예외를_던진다(final String color) {
        // given
        Member 매트 = 매트();
        Category 매트_아고라 = 매트_아고라(매트);
        Subscription subscription = 빨간색_구독(매트, 매트_아고라);

        // when & then
        assertThatThrownBy(() -> subscription.change(color, subscription.isChecked()))
                .isInstanceOf(InvalidSubscriptionException.class);
    }

    @DisplayName("구독의 체크 유무를 수정한다.")
    @Test
    void 구독의_체크_유무를_수정한다() {
        // given
        Member 매트 = 매트();
        Category 매트_아고라 = 매트_아고라(매트);

        // when
        Subscription actual = 빨간색_구독(매트, 매트_아고라);
        actual.change(actual.getColor(), !actual.isChecked());

        // then
        assertAll(() -> {
            assertThat(actual.isChecked()).isFalse();
        });
    }
}
