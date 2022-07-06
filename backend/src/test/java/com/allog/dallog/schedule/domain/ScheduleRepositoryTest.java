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
}
