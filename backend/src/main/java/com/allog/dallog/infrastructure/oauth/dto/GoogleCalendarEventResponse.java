package com.allog.dallog.infrastructure.oauth.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class GoogleCalendarEventResponse {

    private String id;
    private String summary = "";
    private String description = "";
    private GoogleDateFormat start;
    private GoogleDateFormat end;

    private GoogleCalendarEventResponse() {
    }

    public GoogleCalendarEventResponse(final String id, final String summary, final String description,
                                       final GoogleDateFormat start,
                                       final GoogleDateFormat end) {
        this.id = id;
        this.summary = summary;
        this.description = description;
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStartDateTime() {
        if (Objects.isNull(start.getDate())) {
            return LocalDateTime.parse(start.getDateTime().substring(0, 19));
        }

        return LocalDateTime.of(LocalDate.parse(start.getDate()), LocalTime.MIN);
    }

    public LocalDateTime getEndDateTime() {
        if (Objects.isNull(end.getDate())) {
            return LocalDateTime.parse(end.getDateTime().substring(0, 19));
        }

        return LocalDateTime.of(LocalDate.parse(end.getDate()), LocalTime.MIN);
    }

    public String getId() {
        return id;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public GoogleDateFormat getStart() {
        return start;
    }

    public GoogleDateFormat getEnd() {
        return end;
    }
}
