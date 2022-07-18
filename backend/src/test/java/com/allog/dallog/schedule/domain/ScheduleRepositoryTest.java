package com.allog.dallog.schedule.domain;

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
        Schedule schedule1 = new Schedule("알록1", LocalDateTime.of(2022, 7, 15, 14, 20),
                LocalDateTime.of(2022, 7, 15, 16, 20), "달록");
        Schedule schedule2 = new Schedule("알록2", LocalDateTime.of(2022, 8, 15, 14, 20),
                LocalDateTime.of(2022, 8, 15, 16, 20), "달록");

        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);

        LocalDateTime startDate = LocalDateTime.of(2022, 7, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2022, 7, 31, 0, 0);

        // when
        List<Schedule> schedules = scheduleRepository.findByBetween(startDate, endDate);

        // then
        assertThat(schedules).hasSize(1);
    }

    @DisplayName("시작일시와 종료일시를 전달할 때 일정의 시작날짜가 시작일시와 같으면 조회된다.")
    @Test
    void 시작일시와_종료일시를_전달할_때_일정의_시작날짜가_시작일시와_같으면_조회된다() {
        // given
        Schedule schedule1 = new Schedule("알록1", LocalDateTime.of(2022, 7, 1, 0, 0),
                LocalDateTime.of(2022, 7, 15, 16, 20), "달록");
        Schedule schedule2 = new Schedule("알록2", LocalDateTime.of(2022, 8, 15, 14, 20),
                LocalDateTime.of(2022, 8, 15, 16, 20), "달록");

        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);

        LocalDateTime startDate = LocalDateTime.of(2022, 7, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2022, 7, 31, 0, 0);

        // when
        List<Schedule> schedules = scheduleRepository.findByBetween(startDate, endDate);

        // then
        assertThat(schedules).hasSize(1);
    }

    @DisplayName("시작일시와 종료일시를 전달할 때 일정의 시작날짜가 종료일시와 같으면 조회된다.")
    @Test
    void 시작일시와_종료일시를_전달할_때_일정의_시작날짜가_종료일시와_같으면_조회된다() {
        // given
        Schedule schedule1 = new Schedule("알록1", LocalDateTime.of(2022, 7, 31, 0, 0),
                LocalDateTime.of(2022, 8, 12, 13, 10), "달록");
        Schedule schedule2 = new Schedule("알록2", LocalDateTime.of(2022, 8, 15, 14, 20),
                LocalDateTime.of(2022, 8, 15, 16, 20), "달록");

        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);

        LocalDateTime startDate = LocalDateTime.of(2022, 7, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2022, 7, 31, 0, 0);

        // when
        List<Schedule> schedules = scheduleRepository.findByBetween(startDate, endDate);

        // then
        assertThat(schedules).hasSize(1);
    }

    @DisplayName("시작일시와 종료일시를 전달할 때 일정의 시작날짜가 종료일시 이후이면 조회되지 않는다.")
    @Test
    void 시작일시와_종료일시를_전달할_때_일정의_시작날짜가_종료일시_이후이면_조회되지_않는다() {
        // given
        Schedule schedule1 = new Schedule("알록1", LocalDateTime.of(2022, 7, 31, 0, 1),
                LocalDateTime.of(2022, 8, 12, 13, 10), "달록");
        Schedule schedule2 = new Schedule("알록2", LocalDateTime.of(2022, 8, 15, 14, 20),
                LocalDateTime.of(2022, 8, 15, 16, 20), "달록");

        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);

        LocalDateTime startDate = LocalDateTime.of(2022, 7, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2022, 7, 31, 0, 0);

        // when
        List<Schedule> schedules = scheduleRepository.findByBetween(startDate, endDate);

        // then
        assertThat(schedules).hasSize(0);
    }

    @DisplayName("시작일시와 종료일시를 전달할 때 일정의 종료날짜가 시작일시와 같으면 조회된다.")
    @Test
    void 시작일시와_종료일시를_전달할_때_일정의_종료날짜가_시작일시와_같으면_조회된다() {
        // given
        Schedule schedule1 = new Schedule("알록1", LocalDateTime.of(2022, 6, 12, 13, 40),
                LocalDateTime.of(2022, 7, 1, 0, 0), "달록");
        Schedule schedule2 = new Schedule("알록2", LocalDateTime.of(2022, 8, 15, 14, 20),
                LocalDateTime.of(2022, 8, 15, 16, 20), "달록");

        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);

        LocalDateTime startDate = LocalDateTime.of(2022, 7, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2022, 7, 31, 0, 0);

        // when
        List<Schedule> schedules = scheduleRepository.findByBetween(startDate, endDate);

        // then
        assertThat(schedules).hasSize(1);
    }

    @DisplayName("시작일시와 종료일시를 전달할 때 일정의 종료날짜가 시작일시 이전이면 조회되지 않는다.")
    @Test
    void 시작일시와_종료일시를_전달할_때_일정의_종료날짜가_시작일시_이전이면_조회되지_않는다() {
        // given
        Schedule schedule1 = new Schedule("알록1", LocalDateTime.of(2022, 6, 12, 13, 40),
                LocalDateTime.of(2022, 6, 30, 23, 59), "달록");
        Schedule schedule2 = new Schedule("알록2", LocalDateTime.of(2022, 8, 15, 14, 20),
                LocalDateTime.of(2022, 8, 15, 16, 20), "달록");

        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);

        LocalDateTime startDate = LocalDateTime.of(2022, 7, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2022, 7, 31, 0, 0);

        // when
        List<Schedule> schedules = scheduleRepository.findByBetween(startDate, endDate);

        // then
        assertThat(schedules).hasSize(0);
    }
}
