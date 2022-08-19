package com.allog.dallog.infrastructure.oauth.dto;

import java.util.List;

public class GoogleCalendarEventsResponse {

    private String kind;
    private String etag;
    private String summary;
    private String description;
    private String timeZone;
    private String accessRole;
    private String nextPageToken;
    private String nextSyncToken;
    private List<GoogleCalendarEventResponse> items;

    private GoogleCalendarEventsResponse() {
    }

    public GoogleCalendarEventsResponse(final String kind, final String etag, final String summary,
                                        final String description, final String timeZone, final String accessRole,
                                        final String nextPageToken, final String nextSyncToken,
                                        final List<GoogleCalendarEventResponse> items) {
        this.kind = kind;
        this.etag = etag;
        this.summary = summary;
        this.description = description;
        this.timeZone = timeZone;
        this.accessRole = accessRole;
        this.nextPageToken = nextPageToken;
        this.nextSyncToken = nextSyncToken;
        this.items = items;
    }

    public String getKind() {
        return kind;
    }

    public String getEtag() {
        return etag;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getAccessRole() {
        return accessRole;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public String getNextSyncToken() {
        return nextSyncToken;
    }

    public List<GoogleCalendarEventResponse> getItems() {
        return items;
    }
}
