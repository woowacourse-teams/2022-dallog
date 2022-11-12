package com.allog.dallog.schedule.dto.request;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateRangeRequest {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm";

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public DateRangeRequest(final String startDateTime, final String endDateTime) {
        this.startDateTime = LocalDateTime.parse(startDateTime, DateTimeFormatter.ofPattern(DATE_FORMAT));
        this.endDateTime = LocalDateTime.parse(endDateTime, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static DateRangeRequest of(final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        String startDateTimeFormat = startDateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        String endDateTimeFormat = endDateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT));

        return new DateRangeRequest(startDateTimeFormat, endDateTimeFormat);
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }
}
