package com.allog.dallog.domain.schedule.domain.repeat;

import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정;
import static com.allog.dallog.common.fixtures.MemberFixtures.관리자;
import static com.allog.dallog.domain.schedule.domain.repeat.strategy.RepeatType.EVERY_DAY;
import static com.allog.dallog.domain.schedule.domain.repeat.strategy.RepeatType.EVERY_MONTH;
import static com.allog.dallog.domain.schedule.domain.repeat.strategy.RepeatType.EVERY_WEEK;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.domain.schedule.domain.Schedule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScheduleRepeatGroupTest {

    @DisplayName("생성자에 최초 일정과 반복 종료 날짜, 반복 유형으로 EVERY_DAY를 전달하면 반복 일정을 생성하고 저장한다.")
    @Test
    void 생성자에_최초_일정과_반복_종료_날짜_반복_유형으로_EVERY_DAY를_전달하면_반복_일정을_생성하고_저장한다() {
        // given
        Schedule initialSchedule = new Schedule(공통_일정(관리자()), "공부", LocalDateTime.of(2022, 8, 1, 10, 0),
                LocalDateTime.of(2022, 8, 1, 18, 0), "학습하는 날");
        LocalDate endDate = LocalDate.of(2022, 8, 31);

        ScheduleRepeatGroup scheduleRepeatGroup = new ScheduleRepeatGroup(initialSchedule, endDate, EVERY_DAY);

        // when
        List<Schedule> schedules = scheduleRepeatGroup.getSchedules();

        // then
        assertThat(schedules).hasSize(31);
    }

    @DisplayName("생성자에 최초 일정과 반복 종료 날짜, 반복 유형으로 EVERY_WEEK를 전달하면 반복 일정을 생성하고 저장한다.")
    @Test
    void 생성자에_최초_일정과_반복_종료_날짜_반복_유형으로_EVERY_WEEK를_전달하면_반복_일정을_생성하고_저장한다() {
        // given
        Schedule initialSchedule = new Schedule(공통_일정(관리자()), "공부", LocalDateTime.of(2022, 8, 1, 10, 0),
                LocalDateTime.of(2022, 8, 1, 18, 0), "학습하는 날");
        LocalDate endDate = LocalDate.of(2022, 8, 31);

        ScheduleRepeatGroup scheduleRepeatGroup = new ScheduleRepeatGroup(initialSchedule, endDate, EVERY_WEEK);

        // when
        List<Schedule> schedules = scheduleRepeatGroup.getSchedules();

        // then
        assertThat(schedules).hasSize(5);
    }

    @DisplayName("생성자에 최초 일정과 반복 종료 날짜, 반복 유형으로 EVERY_MONTH를 전달하면 반복 일정을 생성하고 저장한다.")
    @Test
    void 생성자에_최초_일정과_반복_종료_날짜_반복_유형으로_EVERY_MONTH를_전달하면_반복_일정을_생성하고_저장한다() {
        // given
        Schedule initialSchedule = new Schedule(공통_일정(관리자()), "공부", LocalDateTime.of(2022, 8, 1, 10, 0),
                LocalDateTime.of(2022, 8, 1, 18, 0), "학습하는 날");
        LocalDate endDate = LocalDate.of(2022, 9, 30);

        ScheduleRepeatGroup scheduleRepeatGroup = new ScheduleRepeatGroup(initialSchedule, endDate, EVERY_MONTH);

        // when
        List<Schedule> schedules = scheduleRepeatGroup.getSchedules();

        // then
        assertThat(schedules).hasSize(2);
    }
}
