package com.allog.dallog.infrastructure.oauth.dto;

import java.util.List;

public class GoogleCalendarEventsResponse {

    private List<GoogleCalendarEventResponse> items;

    private GoogleCalendarEventsResponse() {
    }

    public GoogleCalendarEventsResponse(final List<GoogleCalendarEventResponse> items) {
        this.items = items;
    }

    public List<GoogleCalendarEventResponse> getItems() {
        return items;
    }
}
