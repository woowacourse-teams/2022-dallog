package com.allog.dallog.domain.subscription.domain;

import static com.allog.dallog.common.fixtures.CategoryFixtures.매트_아고라;
import static com.allog.dallog.common.fixtures.CategoryFixtures.후디_JPA_스터디;
import static com.allog.dallog.common.fixtures.MemberFixtures.매트;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.색상1_구독;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SubscriptionTest {

    @DisplayName("구독을 생성한다.")
    @Test
    void 구독을_생성한다() {
        // given
        Member 후디 = 후디();
        Category 후디_JPA_스터디 = 후디_JPA_스터디(후디);
        Color color = Color.COLOR_1;

        // when & then
        assertDoesNotThrow(() -> new Subscription(후디, 후디_JPA_스터디, color));
    }

    @DisplayName("구독이 생성되면 기본적으로 체크된다.")
    @Test
    void 구독이_생성되면_기본적으로_체크된다() {
        // given
        Member 매트 = 매트();
        Category 매트_아고라 = 매트_아고라(매트);
        Color color = Color.COLOR_1;

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
        Subscription actual = 색상1_구독(매트, 매트_아고라);
        actual.change(Color.COLOR_1, actual.isChecked());

        // then
        assertThat(actual.getColor()).isEqualTo(Color.COLOR_1);
    }

    @DisplayName("구독의 체크 유무를 수정한다.")
    @Test
    void 구독의_체크_유무를_수정한다() {
        // given
        Member 매트 = 매트();
        Category 매트_아고라 = 매트_아고라(매트);

        // when
        Subscription actual = 색상1_구독(매트, 매트_아고라);
        actual.change(actual.getColor(), !actual.isChecked());

        // then
        assertThat(actual.isChecked()).isFalse();
    }
}
