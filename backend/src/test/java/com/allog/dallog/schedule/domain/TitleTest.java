package com.allog.dallog.schedule.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.allog.dallog.schedule.exception.InvalidScheduleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TitleTest {

    @DisplayName("일정 제목의 길이가 20을 초과하는 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"일이삼사오육칠팔구십일이삼사오육칠팔구십일",
        "알록달록 알록달록 알록달록 알록달록 알록달록 알록달록 알록달록 회의"})
    void 일정_제목의_길이가_20을_초과하는_경우_예외를_던진다(final String title) {
        // when & then
        assertThatThrownBy(() -> new Title(title))
            .isInstanceOf(InvalidScheduleException.class);
    }
}
