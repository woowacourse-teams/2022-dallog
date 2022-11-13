package com.allog.dallog.schedule.domain;

import static com.allog.dallog.category.domain.CategoryType.NORMAL;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_메모;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_시작일시;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_제목;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_종료일시;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IntegrationScheduleTest {

    @DisplayName("일정을 생성한다.")
    @Test
    void 일정을_생성한다() {
        // given
        String id = "1";
        Long categoryId = 1L;

        // when & then
        assertDoesNotThrow(
                () -> new IntegrationSchedule(id, categoryId, 알록달록_회의_제목, 알록달록_회의_시작일시, 알록달록_회의_종료일시, 알록달록_회의_메모,
                        NORMAL));
    }

    @DisplayName("LongTerm인지 확인 할 때, AllDays가 아니고 일정의 시작일과 종료일이 다르면 true를 반환한다.")
    @Test
    void LongTerm인지_확인_할_때_AllDays가_아니고_일정의_시작일과_종료일이_다르면_true를_반환한다() {
        // given
        String id = "1";
        Long categoryId = 1L;
        IntegrationSchedule integrationSchedule = new IntegrationSchedule(id, categoryId, 알록달록_회의_제목,
                LocalDateTime.of(2022, 7, 1, 0, 1),
                LocalDateTime.of(2022, 7, 2, 0, 0), 알록달록_회의_메모, NORMAL);

        // when
        boolean actual = integrationSchedule.isLongTerms();

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("LongTerm인지 확인 할 때, 일정의 시작일과 종료일이 같으면 false를 반환한다.")
    @Test
    void LongTerm인지_확인_할_때_일정의_시작일과_종료일이_같으면_false를_반환한다() {
        // given
        String id = "1";
        Long categoryId = 1L;
        IntegrationSchedule integrationSchedule = new IntegrationSchedule(id, categoryId, 알록달록_회의_제목,
                LocalDateTime.of(2022, 7, 1, 0, 1),
                LocalDateTime.of(2022, 7, 1, 23, 59), 알록달록_회의_메모, NORMAL);

        // when
        boolean actual = integrationSchedule.isLongTerms();

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("LongTerm인지 확인 할 때, 일정의 시작일과 종료일이 달라도 AllDays면 false를 반환한다.")
    @Test
    void LongTerm인지_확인_할_때_일정의_시작일과_종료일이_달라도_AllDays면_false를_반환한다() {
        // given
        String id = "1";
        Long categoryId = 1L;
        IntegrationSchedule integrationSchedule = new IntegrationSchedule(id, categoryId, 알록달록_회의_제목,
                LocalDateTime.of(2022, 7, 1, 0, 0),
                LocalDateTime.of(2022, 7, 2, 0, 0), 알록달록_회의_메모, NORMAL);

        // when
        boolean actual = integrationSchedule.isLongTerms();

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("AllDays인지 확인 할 때, 일정의 일차가 하루고 시작시간과 종료시간 모두 자정이면 true를 반환한다.")
    @Test
    void AllDays인지_확인_할_때_일정의_일차가_하루고_시작시간과_종료시간이_모두_자정이면_true를_반환한다() {
        // given
        String id = "1";
        Long categoryId = 1L;
        IntegrationSchedule integrationSchedule = new IntegrationSchedule(id, categoryId, 알록달록_회의_제목,
                LocalDateTime.of(2022, 7, 1, 0, 0),
                LocalDateTime.of(2022, 7, 2, 0, 0), 알록달록_회의_메모, NORMAL);

        // when
        boolean actual = integrationSchedule.isAllDays();

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("AllDays인지 확인 할 때, 일정의 일차가 하루여도 시작시간과 종료시간이 자정이 아니면 false를 반환한다.")
    @Test
    void AllDays인지_확인_할_때_일정의_일차가_하루여도_시작시간과_종료시간이__자정이_아니면_false를_반환한다() {
        // given
        String id = "1";
        Long categoryId = 1L;
        IntegrationSchedule integrationSchedule = new IntegrationSchedule(id, categoryId, 알록달록_회의_제목,
                LocalDateTime.of(2022, 7, 1, 0, 0),
                LocalDateTime.of(2022, 7, 2, 0, 1), 알록달록_회의_메모, NORMAL);

        // when
        boolean actual = integrationSchedule.isAllDays();

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("FewHours인지 확인 할 때, 일정의 시작일과 종료일이 같으면 true를 반환한다.")
    @Test
    void FewHours인지_확인_할_때_일정의_시작일과_종료일이_같으면_true를_반환한다() {
        // given
        String id = "1";
        Long categoryId = 1L;
        IntegrationSchedule integrationSchedule = new IntegrationSchedule(id, categoryId, 알록달록_회의_제목,
                LocalDateTime.of(2022, 7, 1, 0, 0),
                LocalDateTime.of(2022, 7, 1, 11, 59), 알록달록_회의_메모, NORMAL);

        // when
        boolean actual = integrationSchedule.isFewHours();

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("FewHours인지 확인 할 때, 일정의 시작일과 종료일이 다르면 false를 반환한다.")
    @Test
    void FewHours인지_확인_할_때_일정의_시작일과_종료일이_다르면_false를_반환한다() {
        // given
        String id = "1";
        Long categoryId = 1L;
        IntegrationSchedule integrationSchedule = new IntegrationSchedule(id, categoryId, 알록달록_회의_제목,
                LocalDateTime.of(2022, 7, 1, 0, 0),
                LocalDateTime.of(2022, 7, 2, 0, 0), 알록달록_회의_메모, NORMAL);

        // when
        boolean actual = integrationSchedule.isFewHours();

        // then
        assertThat(actual).isFalse();
    }
}
