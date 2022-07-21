package com.allog.dallog.category.domain;

import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.DISPLAY_NAME;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.EMAIL;
import static com.allog.dallog.common.fixtures.OAuthMemberFixtures.PROFILE_IMAGE_URI;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.allog.dallog.category.exception.InvalidCategoryException;
import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.SocialType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CategoryTest {

    @DisplayName("카테고리를 생성한다.")
    @Test
    void 카테고리를_생성한다() {
        // given
        String name = "BE 공식일정";

        // when & then
        assertDoesNotThrow(
                () -> new Category(name, new Member(EMAIL, PROFILE_IMAGE_URI, DISPLAY_NAME, SocialType.GOOGLE)));
    }

    @DisplayName("카테고리 이름이 공백인 경우 예외를 던진다.")
    @Test
    void 카테고리_이름이_공백인_경우_예외를_던진다() {
        // given
        String name = "";

        // when & then
        assertThatThrownBy(
                () -> new Category(name, new Member(EMAIL, PROFILE_IMAGE_URI, DISPLAY_NAME, SocialType.GOOGLE)))
                .isInstanceOf(InvalidCategoryException.class);
    }

    @DisplayName("카테고리 이름의 길이가 20을 초과하는 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"일이삼사오육칠팔구십일이삼사오육칠팔구십일",
            "알록달록 알록달록 알록달록 알록달록 알록달록 알록달록 카테고리"})
    void 카테고리_이름의_길이가_20을_초과하는_경우_예외를_던진다(final String name) {
        // given & when & then
        assertThatThrownBy(
                () -> new Category(name, new Member(EMAIL, PROFILE_IMAGE_URI, DISPLAY_NAME, SocialType.GOOGLE)))
                .isInstanceOf(InvalidCategoryException.class);
    }
}
