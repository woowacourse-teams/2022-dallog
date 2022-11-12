package com.allog.dallog.common.fixtures;

import com.allog.dallog.externalcalendar.dto.ExternalCalendar;

public class ExternalCalendarFixtures {

    public static final ExternalCalendar 대한민국_공휴일 = new ExternalCalendar(
            "ko.south_korea#holiday@group.v.calendar.google.com", "대한민국 공휴일");

    public static final ExternalCalendar 우아한테크코스 = new ExternalCalendar(
            "en.south_korea#holiday@group.v.calendar.google.com", "우아한테크코스");

    public static final ExternalCalendar 내_일정 = new ExternalCalendar("example@email.com", "내 일정");
}
