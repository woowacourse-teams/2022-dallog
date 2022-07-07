package com.allog.dallog.schedule.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.allog.dallog.schedule.exception.InvalidScheduleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemoTest {

    @DisplayName("일정 메모의 길이가 255를 초과하는 경우 예외를 던진다.")
    @Test
    void 일정_메모의_길이가_255를_초과하는_경우_예외를_던진다() {
        // given
        String memo = "1".repeat(256);

        // when & then
        assertThatThrownBy(() -> new Memo(memo))
            .isInstanceOf(InvalidScheduleException.class);
    }
}
