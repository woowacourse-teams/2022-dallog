package com.allog.dallog.domain.schedule.domain.repeat.strategy;

import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.domain.schedule.domain.Schedule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EveryMonthRepeatStrategyTest {

    private RepeatStrategy repeatStrategy;

    @BeforeEach
    void setUp() {
        repeatStrategy = new EveryMonthRepeatStrategy();
    }

    @DisplayName("최초 일정과 종료 일자를 전달받아 매월 반복되는 일정 리스트를 반환한다.")
    @Test
    void 최초_일정과_종료_일자를_전달받아_매월_반복되는_일정_리스트를_반환한다() {
        // given
        Schedule initialSchedule = new Schedule(공통_일정(관리자()), "공부", LocalDateTime.of(2022, 8, 1, 10, 0),
                LocalDateTime.of(2022, 8, 1, 18, 0), "학습하는 날");
        LocalDate endDate = LocalDate.of(2022, 9, 30);

        // when
        List<Schedule> actual = repeatStrategy.getSchedules(initialSchedule, endDate);

        // then
        assertAll(() -> {
            assertThat(actual).hasSize(2);
        });
    }
}
