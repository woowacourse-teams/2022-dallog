package com.allog.dallog.infrastructure.oauth.dto;

public class GoogleDateFormat {

    private String date;
    private String dateTime;

    private GoogleDateFormat() {
    }

    public GoogleDateFormat(final String date, final String dateTime) {
        this.date = date;
        this.dateTime = dateTime;
    }

    public String getDate() {
        return date;
    }

    public String getDateTime() {
        return dateTime;
    }
}
