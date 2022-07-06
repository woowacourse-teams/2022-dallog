package com.allog.dallog.schedule.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.schedule.dto.request.ScheduleCreateRequest;
import com.allog.dallog.schedule.dto.response.ScheduleResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ScheduleServiceTest {

    @Autowired
    private ScheduleService scheduleService;

    @DisplayName("새로운 일정을 생성한다.")
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

    @DisplayName("연도와 월을 전달받아 해당 월에 해당하는 일정 목록을 가져온다.")
    @Test
    void 연도와_월을_전달받아_해당_월에_해당하는_일정_목록을_가져온다() {
        // given
        ScheduleCreateRequest scheduleCreateRequest1 = new ScheduleCreateRequest("알록달록 회의 1",
            LocalDateTime.of(2022, 7, 5, 12, 30), LocalDateTime.of(2022, 7, 6, 14, 30),
            "알록달록 팀회의 - 선릉 큰 강의실");

        ScheduleCreateRequest scheduleCreateRequest2 = new ScheduleCreateRequest("알록달록 회의 2",
            LocalDateTime.of(2022, 8, 5, 12, 30), LocalDateTime.of(2022, 8, 6, 14, 30),
            "알록달록 팀회의 - 잠실 큰 강의실");

        scheduleService.save(scheduleCreateRequest1);
        scheduleService.save(scheduleCreateRequest2);

        int year = 2022;
        int month = 7;

        // when
        List<ScheduleResponse> schedules = scheduleService.findByYearAndMonth(year, month);

        // then
        assertThat(schedules).hasSize(1);
    }
}
