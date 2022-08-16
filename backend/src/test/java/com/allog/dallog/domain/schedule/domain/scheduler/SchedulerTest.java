package com.allog.dallog.domain.schedule.domain.scheduler;

import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_10일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_10일_11시_59분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_15일_16시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_16시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_16시_1분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_18시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_16일_20시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_1일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_20일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_20일_11시_59분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_27일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_27일_11시_59분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_31일_0시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_7월_7일_16시_0분;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.날짜_2022년_8월_15일_14시_0분;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.schedule.domain.Period;
import com.allog.dallog.domain.schedule.domain.Schedule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SchedulerTest {

    @DisplayName("겹치지 않는 기간을 계산한다.")
    @Test
    void 겹치지_않는_기간을_계산한다() {
        // given
        /* 사람들의 일정 목록 */
        Category 공통_일정 = 공통_일정(관리자());
        String 일정_제목 = "일정 제목";
        String 일정_메모 = "일정 메모";

        Schedule 일정1 = new Schedule(공통_일정, 일정_제목, 날짜_2022년_7월_7일_16시_0분, 날짜_2022년_7월_10일_0시_0분, 일정_메모);
        Schedule 일정2 = new Schedule(공통_일정, 일정_제목, 날짜_2022년_7월_10일_11시_59분, 날짜_2022년_7월_15일_16시_0분, 일정_메모);
        Schedule 일정3 = new Schedule(공통_일정, 일정_제목, 날짜_2022년_7월_16일_16시_0분, 날짜_2022년_7월_16일_16시_1분, 일정_메모);
        Schedule 일정4 = new Schedule(공통_일정, 일정_제목, 날짜_2022년_7월_16일_18시_0분, 날짜_2022년_7월_16일_20시_0분, 일정_메모);
        Schedule 일정5 = new Schedule(공통_일정, 일정_제목, 날짜_2022년_7월_16일_20시_0분, 날짜_2022년_7월_20일_0시_0분, 일정_메모);
        Schedule 일정6 = new Schedule(공통_일정, 일정_제목, 날짜_2022년_7월_20일_11시_59분, 날짜_2022년_7월_27일_0시_0분, 일정_메모);
        Schedule 일정7 = new Schedule(공통_일정, 일정_제목, 날짜_2022년_7월_27일_11시_59분, 날짜_2022년_7월_31일_0시_0분, 일정_메모);
        Schedule 일정8 = new Schedule(공통_일정, 일정_제목, 날짜_2022년_7월_31일_0시_0분, 날짜_2022년_8월_15일_14시_0분, 일정_메모);

        List<Schedule> 일정_목록 = List.of(일정1, 일정2, 일정3, 일정4, 일정5, 일정6, 일정7, 일정8);

        // when
        LocalDate startDate = LocalDate.of(2022, 7, 1);
        LocalDate endDate = LocalDate.of(2022, 8, 31);
        Scheduler scheduler = new Scheduler(일정_목록, startDate, endDate);
        List<Period> actual = scheduler.getPeriods();

        // then
        assertAll(() -> {
            assertThat(actual).hasSize(7);
            assertThat(actual).containsExactly(
                    new Period(날짜_2022년_7월_1일_0시_0분, 날짜_2022년_7월_7일_16시_0분),
                    new Period(날짜_2022년_7월_10일_0시_0분, 날짜_2022년_7월_10일_11시_59분),
                    new Period(날짜_2022년_7월_15일_16시_0분, 날짜_2022년_7월_16일_16시_0분),
                    new Period(날짜_2022년_7월_16일_16시_1분, 날짜_2022년_7월_16일_18시_0분),
                    new Period(날짜_2022년_7월_20일_0시_0분, 날짜_2022년_7월_20일_11시_59분),
                    new Period(날짜_2022년_7월_27일_0시_0분, 날짜_2022년_7월_27일_11시_59분),
                    new Period(날짜_2022년_8월_15일_14시_0분, LocalDateTime.of(endDate, LocalTime.MAX))
            );
        });
    }
}
