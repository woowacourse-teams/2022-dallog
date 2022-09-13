package com.allog.dallog.infrastructure.oauth.dto;

import java.util.List;

public class GoogleCalendarListResponse {

    private List<GoogleCalendarResponse> items;

    private GoogleCalendarListResponse() {
    }

    public GoogleCalendarListResponse(final List<GoogleCalendarResponse> items) {
        this.items = items;
    }

    public List<GoogleCalendarResponse> getItems() {
        return items;
    }
}
