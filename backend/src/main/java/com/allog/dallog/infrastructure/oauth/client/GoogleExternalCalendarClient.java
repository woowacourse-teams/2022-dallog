package com.allog.dallog.infrastructure.oauth.client;

import com.allog.dallog.domain.externalcalendar.application.ExternalCalendarClient;
import com.allog.dallog.domain.externalcalendar.dto.ExternalCalendar;
import com.allog.dallog.domain.schedule.domain.IntegrationSchedule;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class GoogleExternalCalendarClient implements ExternalCalendarClient {

    @Override
    public List<ExternalCalendar> getExternalCalendars(final String accessToken) {
        try {
            Thread.sleep(150);
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
            Thread.sleep(15);
        } catch (final InterruptedException e) {
        }

        return new ArrayList<>();
    }
}
