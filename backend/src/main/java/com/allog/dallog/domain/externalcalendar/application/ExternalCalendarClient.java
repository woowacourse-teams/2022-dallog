package com.allog.dallog.domain.externalcalendar.application;

import com.allog.dallog.domain.externalcalendar.dto.ExternalCalendar;
import com.allog.dallog.domain.externalcalendar.dto.ExternalCalendarSchedule;
import java.util.List;

public interface ExternalCalendarClient {

    List<ExternalCalendar> getExternalCalendars(final String accessToken);

    List<ExternalCalendarSchedule> getExternalCalendarSchedules(final String accessToken,
                                                                final String externalCalendarId,
                                                                final String startDateTime, final String endDateTime);
}
