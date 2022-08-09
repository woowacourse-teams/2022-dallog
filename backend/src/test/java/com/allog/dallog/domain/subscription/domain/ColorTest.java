package com.allog.dallog.domain.subscription.domain;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.allog.dallog.domain.subscription.exception.InvalidSubscriptionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

class ColorTest {

    @DisplayName("랜덤으로 색상을 가져온다.")
    @Test
    void 랜덤으로_색상을_가져온다() {
        // given & when & then
        assertThat(Color.pickAny()).isNotNull();
    }

    @DisplayName("color code에 맞는 색상을 가져온다.")
    @ParameterizedTest
    @EnumSource
    void color_code에_맞는_색상을_가져온다(final Color color) {
        // given & when & then
        assertThat(Color.from(color.getColorCode())).isEqualTo(color);
    }

    @DisplayName("존재하지 않는 color code인 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"#asdfe", "#adfqwerse"})
    void 존재하지_않는_color_code인_경우_예외가_발생한다(final String colorCode) {
        // given & when & then
        assertThatThrownBy(() -> Color.from(colorCode))
                .isInstanceOf(InvalidSubscriptionException.class);
    }
}
