package com.allog.dallog.domain.schedule.dto.request;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateRangeRequest {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private LocalDate startDate;
    private LocalDate endDate;

    public DateRangeRequest(final String startDate, final String endDate) {
        this.startDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern(DATE_FORMAT));
        this.endDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
