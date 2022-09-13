package com.allog.dallog.infrastructure.oauth.dto;

public class GoogleCalendarResponse {

    private String id;
    private String summary;
    private String description;

    private GoogleCalendarResponse() {
    }

    public GoogleCalendarResponse(final String id, final String summary, final String description) {
        this.id = id;
        this.summary = summary;
        this.description = description;
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
}
