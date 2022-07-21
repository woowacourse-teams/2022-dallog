package com.allog.dallog.schedule.domain;

import static com.allog.dallog.common.fixtures.ScheduleFixtures.END_DAY_OF_MONTH;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.MEMO;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.START_DAY_OF_MONTH;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.TITLE;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ScheduleRepositoryTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @DisplayName("시작일시와 종료일시를 전달하면 그 사이에 해당하는 일정을 조회한다.")
    @Test
    void 시작일시와_종료일시를_전달하면_그_사이에_해당하는_일정을_조회한다() {
        // given
        Schedule today = new Schedule(TITLE, LocalDateTime.of(2022, 7, 14, 14, 20),
                LocalDateTime.of(2022, 7, 15, 16, 20), MEMO);

        Schedule nextMonth = new Schedule(TITLE, LocalDateTime.of(2022, 8, 15, 14, 20),
                LocalDateTime.of(2022, 8, 15, 16, 20), MEMO);

        scheduleRepository.save(today);
        scheduleRepository.save(nextMonth);

        // when
        List<Schedule> schedules = scheduleRepository.findByBetween(START_DAY_OF_MONTH, END_DAY_OF_MONTH);

        // then
        assertThat(schedules).hasSize(1);
    }

    @DisplayName("시작일시와 종료일시를 전달할 때 일정의 시작날짜가 시작일시와 같으면 조회된다.")
    @Test
    void 시작일시와_종료일시를_전달할_때_일정의_시작날짜가_시작일시와_같으면_조회된다() {
        // given
        Schedule startDayOfMonth = new Schedule(TITLE, LocalDateTime.of(2022, 7, 1, 0, 0),
                LocalDateTime.of(2022, 7, 15, 16, 20), MEMO);

        Schedule nextMonth = new Schedule(TITLE, LocalDateTime.of(2022, 8, 15, 14, 20),
                LocalDateTime.of(2022, 8, 15, 16, 20), MEMO);

        scheduleRepository.save(startDayOfMonth);
        scheduleRepository.save(nextMonth);

        // when
        List<Schedule> schedules = scheduleRepository.findByBetween(START_DAY_OF_MONTH, END_DAY_OF_MONTH);

        // then
        assertThat(schedules).hasSize(1);
    }

    @DisplayName("시작일시와 종료일시를 전달할 때 일정의 시작날짜가 종료일시와 같으면 조회된다.")
    @Test
    void 시작일시와_종료일시를_전달할_때_일정의_시작날짜가_종료일시와_같으면_조회된다() {
        // given
        Schedule endDayOfMonth = new Schedule(TITLE, START_DAY_OF_MONTH,
                LocalDateTime.of(2022, 8, 12, 13, 10), MEMO);

        Schedule nextMonth = new Schedule(TITLE, LocalDateTime.of(2022, 8, 15, 14, 20),
                LocalDateTime.of(2022, 8, 15, 16, 20), MEMO);

        scheduleRepository.save(endDayOfMonth);
        scheduleRepository.save(nextMonth);

        // when
        List<Schedule> schedules = scheduleRepository.findByBetween(START_DAY_OF_MONTH, END_DAY_OF_MONTH);

        // then
        assertThat(schedules).hasSize(1);
    }

    @DisplayName("시작일시와 종료일시를 전달할 때 일정의 시작날짜가 종료일시 이후이면 조회되지 않는다.")
    @Test
    void 시작일시와_종료일시를_전달할_때_일정의_시작날짜가_종료일시_이후이면_조회되지_않는다() {
        // given
        Schedule afterDayOfMonth = new Schedule(TITLE, LocalDateTime.of(2022, 7, 31, 0, 1),
                LocalDateTime.of(2022, 8, 12, 13, 10), MEMO);

        Schedule nextMonth = new Schedule(TITLE, LocalDateTime.of(2022, 8, 15, 14, 20),
                LocalDateTime.of(2022, 8, 15, 16, 20), MEMO);

        scheduleRepository.save(afterDayOfMonth);
        scheduleRepository.save(nextMonth);

        // when
        List<Schedule> schedules = scheduleRepository.findByBetween(START_DAY_OF_MONTH, END_DAY_OF_MONTH);

        // then
        assertThat(schedules).hasSize(0);
    }

    @DisplayName("시작일시와 종료일시를 전달할 때 일정의 종료날짜가 시작일시와 같으면 조회된다.")
    @Test
    void 시작일시와_종료일시를_전달할_때_일정의_종료날짜가_시작일시와_같으면_조회된다() {
        // given
        Schedule schedule1 = new Schedule(TITLE, LocalDateTime.of(2022, 6, 12, 13, 40),
                START_DAY_OF_MONTH, MEMO);

        Schedule schedule2 = new Schedule(TITLE, LocalDateTime.of(2022, 8, 15, 14, 20),
                LocalDateTime.of(2022, 8, 15, 16, 20), MEMO);

        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);

        // when
        List<Schedule> schedules = scheduleRepository.findByBetween(START_DAY_OF_MONTH, END_DAY_OF_MONTH);

        // then
        assertThat(schedules).hasSize(1);
    }

    @DisplayName("시작일시와 종료일시를 전달할 때 일정의 종료날짜가 시작일시 이전이면 조회되지 않는다.")
    @Test
    void 시작일시와_종료일시를_전달할_때_일정의_종료날짜가_시작일시_이전이면_조회되지_않는다() {
        // given
        Schedule schedule1 = new Schedule(TITLE, LocalDateTime.of(2022, 6, 12, 13, 40),
                LocalDateTime.of(2022, 6, 30, 23, 59), MEMO);

        Schedule schedule2 = new Schedule(TITLE, LocalDateTime.of(2022, 8, 15, 14, 20),
                LocalDateTime.of(2022, 8, 15, 16, 20), MEMO);

        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);

        // when
        List<Schedule> schedules = scheduleRepository.findByBetween(START_DAY_OF_MONTH, END_DAY_OF_MONTH);

        // then
        assertThat(schedules).hasSize(0);
    }
}
