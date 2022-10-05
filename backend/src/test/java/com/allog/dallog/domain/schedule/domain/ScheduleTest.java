package com.allog.dallog.domain.schedule.domain;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_메모;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_시작일시;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_제목;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_종료일시;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
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

    @DisplayName("일정 일시가 가능한 범위를 벗어나는 경우 예외를 던진다.")
    @Test
    void 일정_일시가_가능한_범위를_벗어나는_경우_예외를_던진다() {
        //given
        Category BE_일정_카테고리 = BE_일정(관리자());
        LocalDateTime 잘못된_시작_일시 = LocalDateTime.MIN;
        LocalDateTime 잘못된_종료_일시 = LocalDateTime.MAX;

        // when & then
        assertAll(() -> {
                    assertThatThrownBy(
                            () -> new Schedule(BE_일정_카테고리, 알록달록_회의_제목, 잘못된_시작_일시,
                                    알록달록_회의_종료일시, 알록달록_회의_메모)
                    ).isInstanceOf(InvalidScheduleException.class);
                    assertThatThrownBy(
                            () -> new Schedule(BE_일정_카테고리, 알록달록_회의_제목, 알록달록_회의_시작일시,
                                    잘못된_종료_일시, 알록달록_회의_메모)
                    ).isInstanceOf(InvalidScheduleException.class);
                }
        );
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
}
