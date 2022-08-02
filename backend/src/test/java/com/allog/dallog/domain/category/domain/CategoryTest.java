package com.allog.dallog.domain.category.domain;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.MemberFixtures.후디;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.allog.dallog.domain.category.exception.InvalidCategoryException;
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
        assertDoesNotThrow(() -> new Category(name, 후디()));
    }

    @DisplayName("카테고리 이름이 공백인 경우 예외를 던진다.")
    @Test
    void 카테고리_이름이_공백인_경우_예외를_던진다() {
        // given
        String name = "";

        // when & then
        assertThatThrownBy(() -> new Category(name, 후디()))
                .isInstanceOf(InvalidCategoryException.class);
    }

    @DisplayName("카테고리 이름의 길이가 20을 초과하는 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"일이삼사오육칠팔구십일이삼사오육칠팔구십일",
            "알록달록 알록달록 알록달록 알록달록 알록달록 알록달록 카테고리"})
    void 카테고리_이름의_길이가_20을_초과하는_경우_예외를_던진다(final String name) {
        // given & when & then
        assertThatThrownBy(() -> new Category(name, 후디()))
                .isInstanceOf(InvalidCategoryException.class);
    }

    @DisplayName("제공된 멤버의 ID와 카테고리를 생성한 멤버의 ID가 일치하지 않으면 false를 반환한다.")
    @Test
    void 제공된_멤버의_ID와_카테고리를_생성한_멤버의_ID가_일치하지_않으면_false를_반환한다() {
        // given
        Category BE_일정 = BE_일정(관리자());

        // when
        boolean actual = BE_일정.isCreator(999L);

        // then
        assertThat(actual).isFalse();
    }
}
