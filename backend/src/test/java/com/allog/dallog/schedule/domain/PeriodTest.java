package com.allog.dallog.schedule.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
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

    @DisplayName("시작시간과 종료시간이 모두 자정이면 true를 반환한다.")
    @Test
    void 시작시간과_종료시간이_모두_자정이면_true를_반환한다() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 2, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2022, 1, 4, 0, 0);

        Period period = new Period(startDateTime, endDateTime);

        // when
        boolean actual = period.isMidnightToMidnight();

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("시작시간과 종료시간 중 하나라도 자정이 아니면 false를 반환한다.")
    @Test
    void 시작시간과_종료시간_중_하나라도_자정이_아니면_false를_반환한다() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2022, 1, 2, 10, 10);
        LocalDateTime endDateTime = LocalDateTime.of(2022, 1, 4, 0, 0);

        Period period = new Period(startDateTime, endDateTime);

        // when
        boolean actual = period.isMidnightToMidnight();

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("기간 뺄셈시 상대 기간이 우측에 걸쳐있을 때의 결과를 계산한다.")
    @Test
    void 기간_뺄셈시_상대_기간이_우측에_걸쳐있을_때의_결과를_계산한다() {
        // given
        LocalDateTime baseStartDateTime = LocalDateTime.of(2022, 8, 1, 0, 0);
        LocalDateTime baseEndDateTime = LocalDateTime.of(2022, 8, 2, 23, 59);
        Period basePeriod = new Period(baseStartDateTime, baseEndDateTime);

        LocalDateTime otherStartDateTime = LocalDateTime.of(2022, 8, 1, 18, 0);
        LocalDateTime otherEndDateTime = LocalDateTime.of(2022, 8, 3, 18, 0);
        Period otherPeriod = new Period(otherStartDateTime, otherEndDateTime);

        // when
        List<Period> actual = basePeriod.slice(otherPeriod);

        // then
        assertAll(() -> {
            assertThat(actual).hasSize(1);
            assertThat(actual.get(0).getStartDateTime()).isEqualTo(baseStartDateTime);
            assertThat(actual.get(0).getEndDateTime()).isEqualTo(otherStartDateTime);
        });
    }

    @DisplayName("기간 뺄셈시 상대 기간이 좌측에 걸쳐있을 때의 결과를 계산한다.")
    @Test
    void 기간_뺄셈시_상대_기간이_좌측에_걸쳐있을_때의_결과를_계산한다() {
        // given
        LocalDateTime baseStartDateTime = LocalDateTime.of(2022, 8, 2, 0, 0);
        LocalDateTime baseEndDateTime = LocalDateTime.of(2022, 8, 3, 23, 59);
        Period basePeriod = new Period(baseStartDateTime, baseEndDateTime);

        LocalDateTime otherStartDateTime = LocalDateTime.of(2022, 8, 1, 18, 0);
        LocalDateTime otherEndDateTime = LocalDateTime.of(2022, 8, 2, 18, 0);
        Period otherPeriod = new Period(otherStartDateTime, otherEndDateTime);

        // when
        List<Period> actual = basePeriod.slice(otherPeriod);

        // then
        assertAll(() -> {
            assertThat(actual).hasSize(1);
            assertThat(actual.get(0).getStartDateTime()).isEqualTo(otherEndDateTime);
            assertThat(actual.get(0).getEndDateTime()).isEqualTo(baseEndDateTime);
        });
    }

    @DisplayName("기간 뺄셈시 상대 기간이 안쪽에 포함될때 결과를 계산한다.")
    @Test
    void 기간_뺄셈시_상대_기간이_안쪽에_포함될때_결과를_계산한다() {
        // given
        LocalDateTime baseStartDateTime = LocalDateTime.of(2022, 8, 1, 0, 0);
        LocalDateTime baseEndDateTime = LocalDateTime.of(2022, 8, 3, 23, 59);
        Period basePeriod = new Period(baseStartDateTime, baseEndDateTime);

        LocalDateTime otherStartDateTime = LocalDateTime.of(2022, 8, 1, 18, 0);
        LocalDateTime otherEndDateTime = LocalDateTime.of(2022, 8, 2, 18, 0);
        Period otherPeriod = new Period(otherStartDateTime, otherEndDateTime);

        // when
        List<Period> actual = basePeriod.slice(otherPeriod);

        // then
        assertAll(() -> {
            assertThat(actual).hasSize(2);
            assertThat(actual.get(0).getStartDateTime()).isEqualTo(baseStartDateTime);
            assertThat(actual.get(0).getEndDateTime()).isEqualTo(otherStartDateTime);
            assertThat(actual.get(1).getStartDateTime()).isEqualTo(otherEndDateTime);
            assertThat(actual.get(1).getEndDateTime()).isEqualTo(baseEndDateTime);
        });
    }

    @DisplayName("기간 뺄셈시 상대 기간과 완벽히 일치하면 빈 리스트를 반환한다.")
    @Test
    void 기간_뺄셈시_상대_기간과_완벽히_일치하면_빈_리스트를_반환한다() {
        // given
        LocalDateTime baseStartDateTime = LocalDateTime.of(2022, 8, 1, 0, 0);
        LocalDateTime baseEndDateTime = LocalDateTime.of(2022, 8, 3, 23, 59);
        Period basePeriod = new Period(baseStartDateTime, baseEndDateTime);

        LocalDateTime otherStartDateTime = LocalDateTime.of(2022, 8, 1, 0, 0);
        LocalDateTime otherEndDateTime = LocalDateTime.of(2022, 8, 3, 23, 59);
        Period otherPeriod = new Period(otherStartDateTime, otherEndDateTime);

        // when
        List<Period> actual = basePeriod.slice(otherPeriod);

        // then
        assertThat(actual).hasSize(0);
    }

    @DisplayName("기간 뺄셈시 상대 기간과 겹치지 않으면 자기자신을 리스트로 반환한다.")
    @Test
    void 기간_뺄셈시_상대_기간과_겹치지_않으면_자기자신을_리스트로_반환한다() {
        // given
        LocalDateTime baseStartDateTime = LocalDateTime.of(2022, 8, 1, 0, 0);
        LocalDateTime baseEndDateTime = LocalDateTime.of(2022, 8, 2, 0, 0);
        Period basePeriod = new Period(baseStartDateTime, baseEndDateTime);

        LocalDateTime otherStartDateTime = LocalDateTime.of(2022, 8, 3, 0, 0);
        LocalDateTime otherEndDateTime = LocalDateTime.of(2022, 8, 3, 23, 59);
        Period otherPeriod = new Period(otherStartDateTime, otherEndDateTime);

        // when
        List<Period> actual = basePeriod.slice(otherPeriod);

        // then
        assertAll(() -> {
            assertThat(actual).hasSize(1);
            assertThat(actual.get(0)).isEqualTo(basePeriod);
        });
    }
}
