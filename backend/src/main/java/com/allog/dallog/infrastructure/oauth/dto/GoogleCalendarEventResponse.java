package com.allog.dallog.infrastructure.oauth.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class GoogleCalendarEventResponse {

    private String kind;
    private String etag;
    private String id;
    private String status;
    private String htmlLink;
    private String summary;
    private String description;
    private String location;
    private GoogleDateFormat start;
    private GoogleDateFormat end;
    private String recurringEventId;

    private GoogleCalendarEventResponse() {
    }

    public GoogleCalendarEventResponse(final String kind, final String etag, final String id, final String status,
                                       final String htmlLink, final String summary, final String description,
                                       final String location, final GoogleDateFormat start, final GoogleDateFormat end,
                                       final String recurringEventId) {
        this.kind = kind;
        this.etag = etag;
        this.id = id;
        this.status = status;
        this.htmlLink = htmlLink;
        this.summary = summary;
        this.description = description;
        this.location = location;
        this.start = start;
        this.end = end;
        this.recurringEventId = recurringEventId;
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

    public String getKind() {
        return kind;
    }

    public String getEtag() {
        return etag;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getHtmlLink() {
        return htmlLink;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public GoogleDateFormat getStart() {
        return start;
    }

    public GoogleDateFormat getEnd() {
        return end;
    }

    public String getRecurringEventId() {
        return recurringEventId;
    }
}
