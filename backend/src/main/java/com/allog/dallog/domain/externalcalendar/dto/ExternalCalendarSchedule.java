package com.allog.dallog.domain.externalcalendar.dto;

import java.time.LocalDateTime;

public class ExternalCalendarSchedule {

    private String id;
    private String title = "제목 없음";
    private String description = "";
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    private ExternalCalendarSchedule() {
    }

    public ExternalCalendarSchedule(final String id, final String title, final String description,
                                    final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }
}
