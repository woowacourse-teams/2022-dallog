package com.allog.dallog.infrastructure.oauth.dto;

public class GoogleDateFormat {

    private String date;
    private String dateTime;
    private String timeZone;

    private GoogleDateFormat() {
    }

    public GoogleDateFormat(final String date, final String dateTime, final String timeZone) {
        this.date = date;
        this.dateTime = dateTime;
        this.timeZone = timeZone;
    }

    public String getDate() {
        return date;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getTimeZone() {
        return timeZone;
    }
}
