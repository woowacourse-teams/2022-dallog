package com.allog.dallog.domain.schedule.domain;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_메모;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_시작일시;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_제목;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_종료일시;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.schedule.exception.InvalidScheduleException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ScheduleTest {

    @DisplayName("일정을 생성한다.")
    @Test
    void 일정을_생성한다() {
        // given
        Category BE_일정_카테고리 = BE_일정(관리자());

        // when & then
        assertDoesNotThrow(() -> new Schedule(BE_일정_카테고리, 알록달록_회의_제목, 알록달록_회의_시작일시, 알록달록_회의_종료일시, 알록달록_회의_메모));
    }

    @DisplayName("일정 제목의 길이가 50을 초과하는 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"일이삼사오육칠팔구십일이삼사오육칠팔구십일일이삼사오육칠팔구십일이삼사오육칠팔구십일일이삼사오육칠팔구십일",
            "알록달록 알록달록 알록달록 알록달록 알록달록 알록달록 알록달록 알록달록 알록달록 알록달록 알록달록 회의"})
    void 일정_제목의_길이가_50을_초과하는_경우_예외를_던진다(final String 잘못된_일정_제목) {
        //given
        Category BE_일정_카테고리 = BE_일정(관리자());

        // when & then
        assertThatThrownBy(() -> new Schedule(BE_일정_카테고리, 잘못된_일정_제목, 알록달록_회의_시작일시, 알록달록_회의_종료일시, 알록달록_회의_메모))
                .isInstanceOf(InvalidScheduleException.class);
    }

    @DisplayName("일정 메모의 길이가 255를 초과하는 경우 예외를 던진다.")
    @Test
    void 일정_메모의_길이가_255를_초과하는_경우_예외를_던진다() {
        // given
        String 잘못된_메모 = "1".repeat(256);
        Category BE_일정_카테고리 = BE_일정(관리자());

        // when & then
        assertThatThrownBy(() -> new Schedule(BE_일정_카테고리, 알록달록_회의_제목, 알록달록_회의_시작일시, 알록달록_회의_종료일시, 잘못된_메모))
                .isInstanceOf(InvalidScheduleException.class);
    }

    @DisplayName("일정의 시작일시와 종료일시 사이의 시간 차이를 반환한다.")
    @Test
    void 일정의_시작일시와_종료일시_사이의_시간_차이를_반환한다() {
        // given
        Schedule schedule = new Schedule(BE_일정(관리자()), 알록달록_회의_제목, LocalDateTime.of(2022, 7, 1, 0, 1),
                LocalDateTime.of(2022, 7, 2, 0, 0), 알록달록_회의_메모);

        // when
        long actual = schedule.calculateHourDifference();

        // then
        assertThat(actual).isEqualTo(23);
    }

    @DisplayName("일정의 시작일시와 종료일시 사이의 날짜가 차이나면 true를 반환한다.")
    @Test
    void 일정의_시작일시와_종료일시_사이의_날짜가_차이나면_true를_반환한다() {
        // given
        Schedule schedule = new Schedule(BE_일정(관리자()), 알록달록_회의_제목, LocalDateTime.of(2022, 7, 1, 0, 1),
                LocalDateTime.of(2022, 7, 2, 0, 0), 알록달록_회의_메모);

        // when
        boolean actual = schedule.isDayDifferent();

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("일정의 시작일시와 종료일시 사이의 날짜가 차이나지 않으면 false를 반환한다.")
    @Test
    void 일정의_시작일시와_종료일시_사이의_날짜가_차이나지_않으면_false를_반환한다() {
        // given
        Schedule schedule = new Schedule(BE_일정(관리자()), 알록달록_회의_제목, LocalDateTime.of(2022, 7, 1, 0, 1),
                LocalDateTime.of(2022, 7, 1, 23, 18), 알록달록_회의_메모);

        // when
        boolean actual = schedule.isDayDifferent();

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("일정의 시작일시와 종료일시가 둘다 자정(0시 0분)일 경우 true를 반환한다.")
    @Test
    void 일정의_시작일시와_종료일시가_둘다_자정일_경우_true를_반환한다() {
        // given
        Schedule schedule = new Schedule(BE_일정(관리자()), 알록달록_회의_제목, LocalDateTime.of(2022, 7, 1, 0, 0),
                LocalDateTime.of(2022, 7, 2, 0, 0), 알록달록_회의_메모);

        // when
        boolean actual = schedule.isMidNightToMidNight();

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("일정의 시작일시와 종료일시가 둘다 자정(0시 0분)이 아닐 경우 false를 반환한다.")
    @Test
    void 일정의_시작일시와_종료일시가_둘다_자정이_아닐_경우_false를_반환한다() {
        // given
        Schedule schedule = new Schedule(BE_일정(관리자()), 알록달록_회의_제목, LocalDateTime.of(2022, 7, 1, 1, 0),
                LocalDateTime.of(2022, 7, 2, 2, 0), 알록달록_회의_메모);

        // when
        boolean actual = schedule.isMidNightToMidNight();

        // then
        assertThat(actual).isFalse();
    }
}
