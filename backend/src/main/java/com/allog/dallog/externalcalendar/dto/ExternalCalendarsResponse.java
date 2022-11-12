package com.allog.dallog.externalcalendar.dto;

import java.util.List;

public class ExternalCalendarsResponse {

    private List<ExternalCalendar> externalCalendars;

    private ExternalCalendarsResponse() {
    }

    public ExternalCalendarsResponse(final List<ExternalCalendar> externalCalendars) {
        this.externalCalendars = externalCalendars;
    }

    public List<ExternalCalendar> getExternalCalendars() {
        return externalCalendars;
    }
}
