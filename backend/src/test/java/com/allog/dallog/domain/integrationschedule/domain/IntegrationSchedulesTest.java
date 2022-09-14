package com.allog.dallog.domain.integrationschedule.domain;

import static com.allog.dallog.domain.category.domain.CategoryType.NORMAL;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IntegrationSchedulesTest {

    @DisplayName("겹치는 일정이 하나도 없을 때, 일정 시작일시가 빠른 순서대로 정렬된다.")
    @Test
    void 겹치는_일정이_하나도_없을_때_일정_시작일시가_빠른_순서대로_정렬된다() {
        // given
        Long categoryId = 1L;
        IntegrationSchedule 첫번째로_정렬되어야_하는_일정 = new IntegrationSchedule("1", categoryId, "일정1",
                LocalDateTime.of(2022, 3, 1, 0, 0),
                LocalDateTime.of(2022, 3, 2, 0, 0), "일정1", NORMAL);

        IntegrationSchedule 두번째로_정렬되어야_하는_일정 = new IntegrationSchedule("2", categoryId, "일정2",
                LocalDateTime.of(2022, 3, 3, 0, 0),
                LocalDateTime.of(2022, 3, 4, 0, 0), "일정2", NORMAL);

        IntegrationSchedule 세번째로_정렬되어야_하는_일정 = new IntegrationSchedule("3", categoryId, "일정3",
                LocalDateTime.of(2022, 3, 5, 0, 0),
                LocalDateTime.of(2022, 3, 7, 0, 0), "일정3", NORMAL);

        // when
        IntegrationSchedules integrationSchedules = new IntegrationSchedules();
        integrationSchedules.add(세번째로_정렬되어야_하는_일정);
        integrationSchedules.add(두번째로_정렬되어야_하는_일정);
        integrationSchedules.add(첫번째로_정렬되어야_하는_일정);

        // then
        assertThat(integrationSchedules.getSortedValues())
                .extracting(IntegrationSchedule::getTitle)
                .containsExactly("일정1", "일정2", "일정3");
    }

    @DisplayName("일정의 시작일시가 겹친다면, 일정 종료일시가 느린 순서대로 정렬된다.")
    @Test
    void 일정의_시작일시가_겹친다면_일정_종료일시가_느린_순서대로_정렬된다() {
        // given
        Long categoryId = 1L;
        IntegrationSchedule 첫번째로_정렬되어야_하는_일정 = new IntegrationSchedule("1", categoryId, "일정1",
                LocalDateTime.of(2022, 3, 1, 0, 0),
                LocalDateTime.of(2022, 3, 10, 0, 0), "일정1", NORMAL);

        IntegrationSchedule 두번째로_정렬되어야_하는_일정 = new IntegrationSchedule("2", categoryId, "일정2",
                LocalDateTime.of(2022, 3, 1, 0, 0),
                LocalDateTime.of(2022, 3, 7, 0, 0), "일정2", NORMAL);

        IntegrationSchedule 세번째로_정렬되어야_하는_일정 = new IntegrationSchedule("3", categoryId, "일정3",
                LocalDateTime.of(2022, 3, 5, 0, 0),
                LocalDateTime.of(2022, 3, 5, 0, 0), "일정3", NORMAL);

        // when
        IntegrationSchedules integrationSchedules = new IntegrationSchedules();
        integrationSchedules.add(두번째로_정렬되어야_하는_일정);
        integrationSchedules.add(세번째로_정렬되어야_하는_일정);
        integrationSchedules.add(첫번째로_정렬되어야_하는_일정);

        // then
        assertThat(integrationSchedules.getSortedValues())
                .extracting(IntegrationSchedule::getTitle)
                .containsExactly("일정1", "일정2", "일정3");
    }

    @DisplayName("일정의 시작일시가 겹치고, 종료일시도 겹칠때는 일정의 제목을 사전기준 오름차순으로 정렬된다.")
    @Test
    void 일정의_시작일시가_겹치고_종료일시도_겹칠때는_일정의_제목을_사전기준_오름차순으로_정렬된다() {
        // given
        Long categoryId = 1L;
        IntegrationSchedule 첫번째로_정렬되어야_하는_일정 = new IntegrationSchedule("1", categoryId, "가",
                LocalDateTime.of(2022, 3, 1, 0, 0),
                LocalDateTime.of(2022, 3, 10, 0, 0), "일정1", NORMAL);

        IntegrationSchedule 두번째로_정렬되어야_하는_일정 = new IntegrationSchedule("2", categoryId, "나",
                LocalDateTime.of(2022, 3, 1, 0, 0),
                LocalDateTime.of(2022, 3, 10, 0, 0), "일정2", NORMAL);

        IntegrationSchedule 세번째로_정렬되어야_하는_일정 = new IntegrationSchedule("3", categoryId, "다",
                LocalDateTime.of(2022, 3, 1, 0, 0),
                LocalDateTime.of(2022, 3, 10, 0, 0), "일정3", NORMAL);

        // when
        IntegrationSchedules integrationSchedules = new IntegrationSchedules();
        integrationSchedules.add(세번째로_정렬되어야_하는_일정);
        integrationSchedules.add(두번째로_정렬되어야_하는_일정);
        integrationSchedules.add(첫번째로_정렬되어야_하는_일정);

        // then
        assertThat(integrationSchedules.getSortedValues())
                .extracting(IntegrationSchedule::getTitle)
                .containsExactly("가", "나", "다");
    }
}
