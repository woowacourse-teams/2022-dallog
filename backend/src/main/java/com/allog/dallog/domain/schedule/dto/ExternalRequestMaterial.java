package com.allog.dallog.domain.schedule.dto;

public class ExternalRequestMaterial {

    private final String refreshToken;
    private final Long internalCategoryId;
    private final String externalCalendarId;

    public ExternalRequestMaterial(final String refreshToken, final Long internalCategoryId,
                                   final String externalCalendarId) {
        this.refreshToken = refreshToken;
        this.internalCategoryId = internalCategoryId;
        this.externalCalendarId = externalCalendarId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getInternalCategoryId() {
        return internalCategoryId;
    }

    public String getExternalCalendarId() {
        return externalCalendarId;
    }
}
