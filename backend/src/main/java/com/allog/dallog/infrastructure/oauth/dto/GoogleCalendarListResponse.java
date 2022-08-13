package com.allog.dallog.infrastructure.oauth.dto;

import java.util.List;

public class GoogleCalendarListResponse {

    private String kind;
    private String etag;
    private String nextPageToken;
    private String nextSyncToken;
    private List<GoogleCalendarResponse> items;

    private GoogleCalendarListResponse() {
    }

    public GoogleCalendarListResponse(final String kind, final String etag, final String nextPageToken,
                                      final String nextSyncToken, final List<GoogleCalendarResponse> items) {
        this.kind = kind;
        this.etag = etag;
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

    public String getNextPageToken() {
        return nextPageToken;
    }

    public String getNextSyncToken() {
        return nextSyncToken;
    }

    public List<GoogleCalendarResponse> getItems() {
        return items;
    }
}
