package com.allog.dallog.domain.category.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
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
}
