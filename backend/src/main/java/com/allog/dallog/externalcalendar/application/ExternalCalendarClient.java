package com.allog.dallog.externalcalendar.application;

import com.allog.dallog.externalcalendar.dto.ExternalCalendar;
import com.allog.dallog.schedule.domain.IntegrationSchedule;
import java.util.List;

public interface ExternalCalendarClient {

    List<ExternalCalendar> getExternalCalendars(final String accessToken);

    List<IntegrationSchedule> getExternalCalendarSchedules(final String accessToken,
                                                           final Long internalCategoryId,
                                                           final String externalCalendarId,
                                                           final String startDateTime, final String endDateTime);
}
