package com.allog.dallog.domain.integrationschedule.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PeriodTest {

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
}
