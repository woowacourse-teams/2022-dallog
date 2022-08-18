package com.allog.dallog.infrastructure.oauth.dto;

public class GoogleCalendarResponse {

    private String kind;
    private String etag;
    private String id;
    private String summary;
    private String description;
    private String location;
    private String timeZone;
    private String summaryOverride;
    private String colorId;
    private String backgroundColor;
    private String foregroundColor;
    private boolean hidden;
    private boolean selected;
    private String accessRole;
    private boolean primary;
    private boolean deleted;

    private GoogleCalendarResponse() {
    }

    public GoogleCalendarResponse(final String kind, final String etag, final String id, final String summary,
                                  final String description, final String location, final String timeZone,
                                  final String summaryOverride, final String colorId, final String backgroundColor,
                                  final String foregroundColor, final boolean hidden, final boolean selected,
                                  final String accessRole, final boolean primary, final boolean deleted) {
        this.kind = kind;
        this.etag = etag;
        this.id = id;
        this.summary = summary;
        this.description = description;
        this.location = location;
        this.timeZone = timeZone;
        this.summaryOverride = summaryOverride;
        this.colorId = colorId;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        this.hidden = hidden;
        this.selected = selected;
        this.accessRole = accessRole;
        this.primary = primary;
        this.deleted = deleted;
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

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getSummaryOverride() {
        return summaryOverride;
    }

    public String getColorId() {
        return colorId;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getForegroundColor() {
        return foregroundColor;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isSelected() {
        return selected;
    }

    public String getAccessRole() {
        return accessRole;
    }

    public boolean isPrimary() {
        return primary;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
