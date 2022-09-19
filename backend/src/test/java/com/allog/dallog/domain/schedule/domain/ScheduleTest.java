package com.allog.dallog.domain.schedule.domain;

import static com.allog.dallog.common.fixtures.CategoryFixtures.BE_일정;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_메모;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_시작일시;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_제목;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_종료일시;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryType;
import com.allog.dallog.domain.schedule.exception.InvalidScheduleException;
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

    @DisplayName("일정의 카테고리를 외부연동 카테고리로 변경 하려는 경우 예외를 던진다.")
    @Test
    void 일정의_카테고리를_외부연동_카테고리로_변경_하려는_경우_예외를_던진다() {
        // given
        Category BE_일정_카테고리 = BE_일정(관리자());
        Schedule BE_일정 = new Schedule(BE_일정_카테고리, 알록달록_회의_제목, 알록달록_회의_시작일시, 알록달록_회의_종료일시, 알록달록_회의_메모);

        Category 외부_연동_카테고리 = new Category("외부 카테고리", 관리자(), CategoryType.GOOGLE);

        // when & then
        assertThatThrownBy(() -> BE_일정.changeCategory(외부_연동_카테고리))
                .isInstanceOf(InvalidScheduleException.class);
    }
}
