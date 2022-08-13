package com.allog.dallog.domain.externalcalendar.application;

import com.allog.dallog.domain.externalcalendar.dto.ExternalCalendar;
import java.util.List;

public interface ExternalCalendarClient {

    List<ExternalCalendar> getExternalCalendar(final String accessToken);
}
