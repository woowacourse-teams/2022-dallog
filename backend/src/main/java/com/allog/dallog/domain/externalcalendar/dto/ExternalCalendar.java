package com.allog.dallog.domain.externalcalendar.dto;

public class ExternalCalendar {

    private String calendarId;
    private String summary;

    private ExternalCalendar() {
    }

    public ExternalCalendar(final String calendarId, final String summary) {
        this.calendarId = calendarId;
        this.summary = summary;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public String getSummary() {
        return summary;
    }
}
