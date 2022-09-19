package com.allog.dallog.domain.schedule.domain;

import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_메모;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_시작일시;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_제목;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_종료일시;
import static com.allog.dallog.domain.category.domain.CategoryType.NORMAL;
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

    @DisplayName("LongTerm인지 확인 할 떄, 일정의 시작일시와 종료일시가 다르면 true를 반환한다.")
    @Test
    void LongTerm인지_확인_할_떄_일정의_시작일시와_종료일시가_다르면_true를_반환한다() {
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

    @DisplayName("LongTerm인지 확인 할 떄, 일정의 시작일시와 종료일시가 같으면 false를 반환한다.")
    @Test
    void LongTerm인지_확인_할_때_일정의_시작일시와_종료일시가_다르면_false를_반환한다() {
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

    @DisplayName("AllDays인지 확인 할 떄, 일정의 시작일시와 종료일시가 같고 자정이면 true를 반환한다.")
    @Test
    void AllDays인지_확인_할_때_일정의_시작일시와_종료일시가_같고_자정이면_true를_반환한다() {
        // given
        String id = "1";
        Long categoryId = 1L;
        IntegrationSchedule integrationSchedule = new IntegrationSchedule(id, categoryId, 알록달록_회의_제목,
                LocalDateTime.of(2022, 7, 1, 0, 0),
                LocalDateTime.of(2022, 7, 1, 23, 59), 알록달록_회의_메모, NORMAL);

        // when
        boolean actual = integrationSchedule.isAllDays();

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("AllDays인지 확인 할 떄, 일정의 시작일시와 종료일시가 같지만 자정이 아니면 false를 반환한다.")
    @Test
    void AllDays인지_확인_할_때_일정의_시작일시와_종료일시가_같지만_자정이_아니면_false를_반환한다() {
        // given
        String id = "1";
        Long categoryId = 1L;
        IntegrationSchedule integrationSchedule = new IntegrationSchedule(id, categoryId, 알록달록_회의_제목,
                LocalDateTime.of(2022, 7, 1, 0, 0),
                LocalDateTime.of(2022, 7, 1, 11, 58), 알록달록_회의_메모, NORMAL);

        // when
        boolean actual = integrationSchedule.isAllDays();

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("FewHours인지 확인 할 떄, 일정의 시작일시와 종료일시가 같고 자정이 아니면 true를 반환한다.")
    @Test
    void FewHours인지_확인_할_때_일정의_시작일시와_종료일시가_같고_자정이_아니면_true를_반환한다() {
        // given
        String id = "1";
        Long categoryId = 1L;
        IntegrationSchedule integrationSchedule = new IntegrationSchedule(id, categoryId, 알록달록_회의_제목,
                LocalDateTime.of(2022, 7, 1, 0, 0),
                LocalDateTime.of(2022, 7, 1, 11, 58), 알록달록_회의_메모, NORMAL);

        // when
        boolean actual = integrationSchedule.isFewHours();

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("FewHours인지 확인 할 떄, 일정의 시작일시와 종료일시가 같지만 자정이면 false를 반환한다.")
    @Test
    void FewHours인지_확인_할_때_일정의_시작일시와_종료일시가_같지만_자정이면_false를_반환한다() {
        // given
        String id = "1";
        Long categoryId = 1L;
        IntegrationSchedule integrationSchedule = new IntegrationSchedule(id, categoryId, 알록달록_회의_제목,
                LocalDateTime.of(2022, 7, 1, 0, 0),
                LocalDateTime.of(2022, 7, 1, 23, 59), 알록달록_회의_메모, NORMAL);

        // when
        boolean actual = integrationSchedule.isFewHours();

        // then
        assertThat(actual).isFalse();
    }
}
