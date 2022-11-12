package com.allog.dallog.infrastructure.oauth.client;

import static com.allog.dallog.common.fixtures.ExternalCalendarFixtures.내_일정;
import static com.allog.dallog.common.fixtures.ExternalCalendarFixtures.대한민국_공휴일;
import static com.allog.dallog.common.fixtures.ExternalCalendarFixtures.우아한테크코스;
import static com.allog.dallog.common.fixtures.IntegrationScheduleFixtures.레벨3_방학;
import static com.allog.dallog.common.fixtures.IntegrationScheduleFixtures.포수타;

import com.allog.dallog.externalcalendar.application.ExternalCalendarClient;
import com.allog.dallog.externalcalendar.dto.ExternalCalendar;
import com.allog.dallog.schedule.domain.IntegrationSchedule;
import java.util.List;

public class StubExternalCalendarClient implements ExternalCalendarClient {

    @Override
    public List<ExternalCalendar> getExternalCalendars(final String accessToken) {
        return List.of(대한민국_공휴일, 우아한테크코스, 내_일정);
    }

    @Override
    public List<IntegrationSchedule> getExternalCalendarSchedules(final String accessToken,
                                                                  final Long internalCategoryId,
                                                                  final String externalCalendarId,
                                                                  final String startDateTime,
                                                                  final String endDateTime) {
        return List.of(포수타, 레벨3_방학);
    }
}
