package com.allog.dallog.schedule.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ScheduleTest {

    @DisplayName("일정을 생성한다.")
    @Test
    void 일정을_생성한다() {
        // given
        String title = "알록달록 회의";
        LocalDateTime startDateTime = LocalDateTime.of(2022, 7, 5, 12, 30);
        LocalDateTime endDateTime = LocalDateTime.of(2022, 7, 6, 14, 30);
        String memo = "알록달록 팀회의 - 선릉 큰 강의실";

        // when & then
        assertDoesNotThrow(() -> new Schedule(title, startDateTime, endDateTime, memo));
    }
}
