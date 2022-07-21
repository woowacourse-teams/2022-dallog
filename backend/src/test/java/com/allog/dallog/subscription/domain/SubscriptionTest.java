package com.allog.dallog.subscription.domain;

import static com.allog.dallog.common.fixtures.CategoryFixtures.CATEGORY_NAME;
import static com.allog.dallog.common.fixtures.MemberFixtures.DISPLAY_NAME;
import static com.allog.dallog.common.fixtures.MemberFixtures.EMAIL;
import static com.allog.dallog.common.fixtures.MemberFixtures.PROFILE_IMAGE_URI;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.OAUTH_DISPLAY_NAME;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.OAUTH_EMAIL;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.OAUTH_PROFILE_IMAGE_URI;
import static com.allog.dallog.common.fixtures.SubscriptionFixtures.COLOR_RED;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.allog.dallog.category.domain.Category;
import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.SocialType;
import com.allog.dallog.subscription.exception.InvalidSubscriptionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SubscriptionTest {

    private Member member;
    private Category category;
    private String color;

    @BeforeEach
    void setUp() {
        member = new Member(EMAIL, PROFILE_IMAGE_URI, DISPLAY_NAME, SocialType.GOOGLE);
        category = new Category(CATEGORY_NAME, member);
        color = COLOR_RED;
    }

    @DisplayName("구독을 생성한다.")
    @Test
    void 구독을_생성한다() {
        // given & when & then
        assertDoesNotThrow(() -> new Subscription(member, category, color));
    }

    @DisplayName("색 정보 형식이 잘못된 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"#111", "#1111", "#11111", "123456", "#**1234", "##12345", "334172#"})
    void 색_정보_형식이_잘못된_경우_예외를_던진다(final String color) {
        // given & when & then
        assertThatThrownBy(() -> new Subscription(member, category, color))
                .isInstanceOf(InvalidSubscriptionException.class);
    }
}
