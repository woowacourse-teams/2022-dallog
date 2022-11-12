package com.allog.dallog.domain.category.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.category.domain.CategoryType;
import com.allog.dallog.category.exception.NoSuchCategoryException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class CategoryTypeTest {

    @DisplayName("카테고리 종류를 가져온다.")
    @ParameterizedTest
    @EnumSource
    void 카테고리_종류를_가져온다(final CategoryType categoryType) {
        // given & when & then
        assertAll(() -> {
            assertThat(CategoryType.from(categoryType.name())).isEqualTo(categoryType);
            assertThat(CategoryType.from(categoryType.name().toLowerCase())).isEqualTo(categoryType);
        });
    }

    @DisplayName("존재하지 않는 카테고리 종류인 경우 예외를 던진다.")
    @Test
    void 존재하지_않는_카테고리_종류인_경우_예외를_던진다() {
        // given
        String notExistingCategoryType = "존재하지 않는 카테고리 종류";

        // when & then
        assertThatThrownBy(() -> CategoryType.from(notExistingCategoryType))
                .isInstanceOf(NoSuchCategoryException.class);
    }
}
