package com.allog.dallog.infrastructure.oauth.client;

import com.allog.dallog.domain.externalcalendar.application.ExternalCalendarClient;
import com.allog.dallog.domain.externalcalendar.dto.ExternalCalendar;
import com.allog.dallog.domain.schedule.domain.IntegrationSchedule;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleExternalCalendarClient implements ExternalCalendarClient {

    private static final String CALENDAR_LIST_REQUEST_URI = "https://www.googleapis.com/calendar/v3/users/me/calendarList";
    private static final String CALENDAR_EVENTS_REQUEST_URI = "https://www.googleapis.com/calendar/v3/calendars/{calendarId}/events?singleEvents=true&timeMax={timeMax}&timeMin={timeMin}";
    private static final String ACCEPT_HEADER_NAME = "Accept";

    private final RestTemplate restTemplate;

    public GoogleExternalCalendarClient(final RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public List<ExternalCalendar> getExternalCalendars(final String accessToken) {
        try {
            Thread.sleep(1);
        } catch (final InterruptedException e) {
        }

        return new ArrayList<>();
    }

    @Override
    public List<IntegrationSchedule> getExternalCalendarSchedules(final String accessToken,
                                                                  final Long internalCategoryId,
                                                                  final String externalCalendarId,
                                                                  final String startDateTime,
                                                                  final String endDateTime) {
        try {
            Thread.sleep(1);
        } catch (final InterruptedException e) {
        }

        return new ArrayList<>();
    }
}
