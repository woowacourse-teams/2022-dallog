package com.allog.dallog.domain.schedule.domain.schedules;

import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.domain.schedule.domain.Schedule;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LongTermsTest {

    // 1. 겹치는 일정이 하나도 없을 때
    // 2. 시작 일정이 겹치는데, 일정 길이는 다를 때
    // 3. 시작 일정이 겹치고, 일정 길이도 같을 때

    @DisplayName("겹치는 일정이 하나도 없을 때, 일정 시작일시가 빠른 순서대로 정렬된다.")
    @Test
    void 겹치는_일정이_하나도_없을_때_일정_시작일시가_빠른_순서대로_정렬된다() {
        // given
        Schedule 첫번째로_정렬되어야_하는_일정 = new Schedule("일정1", LocalDateTime.of(2022, 3, 1, 0, 0),
                LocalDateTime.of(2022, 3, 2, 0, 0), "일정1", 공통_일정(관리자()));

        Schedule 두번째로_정렬되어야_하는_일정 = new Schedule("일정2", LocalDateTime.of(2022, 3, 3, 0, 0),
                LocalDateTime.of(2022, 3, 4, 0, 0), "일정2", 공통_일정(관리자()));

        Schedule 세번째로_정렬되어야_하는_일정 = new Schedule("일정3", LocalDateTime.of(2022, 3, 5, 0, 0),
                LocalDateTime.of(2022, 3, 7, 0, 0), "일정3", 공통_일정(관리자()));

        // when
        LongTerms longTerms = new LongTerms();
        longTerms.add(세번째로_정렬되어야_하는_일정);
        longTerms.add(두번째로_정렬되어야_하는_일정);
        longTerms.add(첫번째로_정렬되어야_하는_일정);

        // then
        assertThat(longTerms.getSchedules())
                .extracting(Schedule::getTitle)
                .containsExactly("일정1", "일정2", "일정3");
    }
}
