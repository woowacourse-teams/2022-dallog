package com.allog.dallog.common.fixtures;

import com.allog.dallog.domain.externalcalendar.dto.ExternalCalendar;
import com.allog.dallog.domain.externalcalendar.dto.ExternalCalendarSchedule;
import java.time.LocalDateTime;

public class ExternalCalendarFixtures {

    public static final ExternalCalendar 대한민국_공휴일 = new ExternalCalendar(
            "ko.south_korea#holiday@group.v.calendar.google.com", "대한민국 공휴일");

    public static final ExternalCalendar 우아한테크코스 = new ExternalCalendar(
            "e청n.south_korea#holiday@group.v.calendar.google.com", "우아한테크코스");

    public static final ExternalCalendar 내_일정 = new ExternalCalendar("example@email.com", "내 일정");

    public static final ExternalCalendarSchedule 레벨3_방학 = new ExternalCalendarSchedule("gsgadfgqwrtqwerfgasdasdasd",
            "레벨3 방학", "", LocalDateTime.parse("2022-08-20T00:00:00"), LocalDateTime.parse("2022-08-20T00:00:00"));

    public static final ExternalCalendarSchedule 포수타 = new ExternalCalendarSchedule("asgasgasfgadfgdf", "포수타", "",
            LocalDateTime.parse("2022-08-12T14:00:00"), LocalDateTime.parse("2022-08-12T14:30:00"));
}
