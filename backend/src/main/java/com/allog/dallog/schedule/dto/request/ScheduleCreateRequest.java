package com.allog.dallog.schedule.dto.request;

import com.allog.dallog.schedule.domain.Schedule;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

public class ScheduleCreateRequest {

    @NotBlank
    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDateTime;

    @NotBlank
    private String memo;

    private ScheduleCreateRequest() {
    }

    public ScheduleCreateRequest(final String title, final LocalDateTime startDateTime, final LocalDateTime endDateTime,
                                 final String memo) {
        this.title = title;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.memo = memo;
    }

    public Schedule toEntity() {
        return new Schedule(title, startDateTime, endDateTime, memo);
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public String getMemo() {
        return memo;
    }
}
