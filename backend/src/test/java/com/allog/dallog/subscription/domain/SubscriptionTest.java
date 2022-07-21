package com.allog.dallog.subscription.domain;

import static com.allog.dallog.common.fixtures.CategoryFixtures.CATEGORY_NAME;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.DISPLAY_NAME;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.EMAIL;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.PROFILE_IMAGE_URI;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.COLOR_RED;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.allog.dallog.category.domain.Category;
import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.SocialType;
import com.allog.dallog.subscription.exception.InvalidSubscriptionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SubscriptionTest {

    @DisplayName("구독을 생성한다.")
    @Test
    void 구독을_생성한다() {
        // given
        Member member = new Member(EMAIL, PROFILE_IMAGE_URI, DISPLAY_NAME, SocialType.GOOGLE);
        Category category = new Category(CATEGORY_NAME, member);
        String color = COLOR_RED;

        // when & then
        assertDoesNotThrow(() -> new Subscription(member, category, color));
    }

    @DisplayName("색 정보 형식이 잘못된 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"#111", "#1111", "#11111", "123456", "#**1234", "##12345", "334172#"})
    void 색_정보_형식이_잘못된_경우_예외를_던진다(final String color) {
        // given
        Member member = new Member(EMAIL, PROFILE_IMAGE_URI, DISPLAY_NAME, SocialType.GOOGLE);
        Category category = new Category(CATEGORY_NAME, member);

        // when & then
        assertThatThrownBy(() -> new Subscription(member, category, color))
                .isInstanceOf(InvalidSubscriptionException.class);
    }
}
