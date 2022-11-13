package com.allog.dallog.schedule.domain.scheduler;

import static com.allog.dallog.category.domain.CategoryType.NORMAL;
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

import com.allog.dallog.category.domain.Category;
import com.allog.dallog.schedule.domain.IntegrationSchedule;
import com.allog.dallog.schedule.domain.Period;
import java.time.LocalDateTime;
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

        IntegrationSchedule 일정1 = new IntegrationSchedule("1", 공통_일정.getId(), 일정_제목, 날짜_2022년_7월_7일_16시_0분,
                날짜_2022년_7월_10일_0시_0분, 일정_메모, NORMAL);
        IntegrationSchedule 일정2 = new IntegrationSchedule("2", 공통_일정.getId(), 일정_제목, 날짜_2022년_7월_10일_11시_59분,
                날짜_2022년_7월_15일_16시_0분, 일정_메모, NORMAL);
        IntegrationSchedule 일정3 = new IntegrationSchedule("3", 공통_일정.getId(), 일정_제목, 날짜_2022년_7월_16일_16시_0분,
                날짜_2022년_7월_16일_16시_1분, 일정_메모, NORMAL);
        IntegrationSchedule 일정4 = new IntegrationSchedule("4", 공통_일정.getId(), 일정_제목, 날짜_2022년_7월_16일_18시_0분,
                날짜_2022년_7월_16일_20시_0분, 일정_메모, NORMAL);
        IntegrationSchedule 일정5 = new IntegrationSchedule("5", 공통_일정.getId(), 일정_제목, 날짜_2022년_7월_16일_20시_0분,
                날짜_2022년_7월_20일_0시_0분, 일정_메모, NORMAL);
        IntegrationSchedule 일정6 = new IntegrationSchedule("6", 공통_일정.getId(), 일정_제목, 날짜_2022년_7월_20일_11시_59분,
                날짜_2022년_7월_27일_0시_0분, 일정_메모, NORMAL);
        IntegrationSchedule 일정7 = new IntegrationSchedule("7", 공통_일정.getId(), 일정_제목, 날짜_2022년_7월_27일_11시_59분,
                날짜_2022년_7월_31일_0시_0분, 일정_메모, NORMAL);
        IntegrationSchedule 일정8 = new IntegrationSchedule("8", 공통_일정.getId(), 일정_제목, 날짜_2022년_7월_31일_0시_0분,
                날짜_2022년_8월_15일_14시_0분, 일정_메모, NORMAL);

        List<IntegrationSchedule> 일정_목록 = List.of(일정1, 일정2, 일정3, 일정4, 일정5, 일정6, 일정7, 일정8);

        // when
        LocalDateTime startDateTime = LocalDateTime.of(2022, 7, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2022, 8, 31, 0, 0);
        Scheduler scheduler = new Scheduler(일정_목록, startDateTime, endDateTime);
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
                    new Period(날짜_2022년_8월_15일_14시_0분, endDateTime)
            );
        });
    }
}
