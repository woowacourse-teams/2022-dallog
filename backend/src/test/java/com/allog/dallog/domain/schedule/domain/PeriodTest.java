package com.allog.dallog.domain.schedule.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

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

    @DisplayName("시작일시와 종료일시의 날짜 차이를 반환한다.")
    @Test
    void 시작일시와_종료일시의_날짜_차이를_반환한다() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 2, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2022, 1, 4, 0, 0);

        Period period = new Period(startDateTime, endDateTime);

        // when
        long dayDifference = period.calculateDayDifference();

        // then
        assertThat(dayDifference).isEqualTo(2);
    }

    @DisplayName("시작일시와 종료일시의 시간 차이를 반환한다.")
    @Test
    void 시작일시와_종료일시의_시간_차이를_반환한다() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 2, 11, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2022, 1, 4, 23, 0);

        Period period = new Period(startDateTime, endDateTime);

        // when
        long hourDifference = period.calculateHourDifference();

        // then
        assertThat(hourDifference).isEqualTo(12);
    }

    @DisplayName("시작일시와 종료일시의 분 차이를 반환한다.")
    @Test
    void 시작일시와_종료일시의_분_차이를_반환한다() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 2, 11, 17);
        LocalDateTime endDateTime = LocalDateTime.of(2022, 1, 4, 11, 19);

        Period period = new Period(startDateTime, endDateTime);

        // when
        long minuteDifference = period.calculateMinuteDifference();

        // then
        assertThat(minuteDifference).isEqualTo(2);
    }

    @DisplayName("시작일시와 종료일시에 일을 더해 반환한다.")
    @Test
    void 시작일시와_종료일시에_일을_더해_반환한다() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2022, 1, 2, 6, 0);
        Period period = new Period(startDateTime, endDateTime);

        // when
        Period actual = period.plusDays(5);

        // then
        assertAll(() -> {
            assertThat(actual.getStartDateTime()).isEqualTo(LocalDateTime.of(2022, 1, 6, 0, 0));
            assertThat(actual.getEndDateTime()).isEqualTo(LocalDateTime.of(2022, 1, 7, 6, 0));
        });
    }

    @DisplayName("시작일시와 종료일시에 주를 더해 반환한다.")
    @Test
    void 시작일시와_종료일시에_주를_더해_반환한다() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2022, 1, 2, 6, 0);
        Period period = new Period(startDateTime, endDateTime);

        // when
        Period actual = period.plusWeeks(2);

        // then
        assertAll(() -> {
            assertThat(actual.getStartDateTime()).isEqualTo(LocalDateTime.of(2022, 1, 15, 0, 0));
            assertThat(actual.getEndDateTime()).isEqualTo(LocalDateTime.of(2022, 1, 16, 6, 0));
        });
    }

    @DisplayName("시작일시와 종료일시에 월을 더해 반환한다.")
    @Test
    void 시작일시와_종료일시에_월을_더해_반환한다() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2022, 1, 2, 6, 0);
        Period period = new Period(startDateTime, endDateTime);

        // when
        Period actual = period.plusMonths(2);

        // then
        assertAll(() -> {
            assertThat(actual.getStartDateTime()).isEqualTo(LocalDateTime.of(2022, 3, 1, 0, 0));
            assertThat(actual.getEndDateTime()).isEqualTo(LocalDateTime.of(2022, 3, 2, 6, 0));
        });
    }
}
