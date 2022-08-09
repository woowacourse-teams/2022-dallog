package com.allog.dallog.domain.schedule.domain.repeat.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.allog.dallog.domain.schedule.exception.InvalidRepeatTypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RepeatTypeTest {

    @DisplayName("반복 타입의 이름을 넣으면 해당하는 인스턴스를 반환한다.")
    @CsvSource(value = {"everyDay,EVERY_DAY", "everyWeek,EVERY_WEEK", "everyMonth,EVERY_MONTH"})
    @ParameterizedTest
    void 반복_타입의_이름을_넣으면_해당하는_인스턴스를_반환한다(String actual, RepeatType expected) {
        // given & when & then
        assertThat(RepeatType.from(actual)).isEqualTo(expected);
    }

    @DisplayName("존재하지 않는 반복 타입의 이름을 전달하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_반복_타입의_이름을_전달하면_예외가_발생한다() {
        // given & when & then
        assertThatThrownBy(() -> RepeatType.from("EVERY_YEAR"))
                .isInstanceOf(InvalidRepeatTypeException.class);
    }
}
