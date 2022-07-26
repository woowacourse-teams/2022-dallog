package com.allog.dallog.domain.schedule.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.allog.dallog.domain.schedule.domain.Period;
import com.allog.dallog.domain.schedule.exception.InvalidScheduleException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PeriodTest {

    @DisplayName("종료일시가 시작일시 이전이라면 예외를 던진다.")
    @Test
    void 종료일시가_시작일시_이전이라면_예외를_던진다() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 2, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2022, 1, 1, 0, 0);

        // when & then
        assertThatThrownBy(() -> new Period(startDateTime, endDateTime))
            .isInstanceOf(InvalidScheduleException.class);
    }
}
