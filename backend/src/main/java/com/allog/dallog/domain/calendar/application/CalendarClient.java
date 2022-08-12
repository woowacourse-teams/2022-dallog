package com.allog.dallog.domain.calendar.application;

import com.allog.dallog.domain.calendar.dto.ExternalCalendar;
import java.util.List;

public interface CalendarClient {

    List<ExternalCalendar> getExternalCalendar(final String accessToken);
}
