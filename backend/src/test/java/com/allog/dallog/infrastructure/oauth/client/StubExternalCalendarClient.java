package com.allog.dallog.infrastructure.oauth.client;

import static com.allog.dallog.common.fixtures.ExternalCalendarFixtures.내_일정;
import static com.allog.dallog.common.fixtures.ExternalCalendarFixtures.대한민국_공휴일;
import static com.allog.dallog.common.fixtures.ExternalCalendarFixtures.우아한테크코스;

import com.allog.dallog.domain.externalcalendar.application.ExternalCalendarClient;
import com.allog.dallog.domain.externalcalendar.dto.ExternalCalendar;
import java.util.List;

public class StubExternalCalendarClient implements ExternalCalendarClient {

    @Override
    public List<ExternalCalendar> getExternalCalendar(final String accessToken) {
        return List.of(대한민국_공휴일, 우아한테크코스, 내_일정);
    }
}
