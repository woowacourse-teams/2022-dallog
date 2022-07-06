package com.allog.dallog.schedule.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.schedule.dto.request.ScheduleCreateRequest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ScheduleServiceTest {

    @Autowired
    private ScheduleService scheduleService;

    @DisplayName("새로운 일정을 생성한다")
    @Test
    void 새로운_일정을_생성한다() {
        // given
        String title = "알록달록 회의";
        LocalDateTime startDateTime = LocalDateTime.of(2022, 7, 5, 12, 30);
        LocalDateTime endDateTime = LocalDateTime.of(2022, 7, 6, 14, 30);
        String memo = "알록달록 팀회의 - 선릉 큰 강의실";

        ScheduleCreateRequest scheduleCreateRequest = new ScheduleCreateRequest(title,
            startDateTime, endDateTime, memo);

        // when
        Long id = scheduleService.save(scheduleCreateRequest);

        // then
        assertThat(id).isNotNull();
    }
}
