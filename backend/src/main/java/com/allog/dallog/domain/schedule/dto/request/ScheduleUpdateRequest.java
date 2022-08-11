package com.allog.dallog.domain.schedule.dto.request;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

public class ScheduleUpdateRequest {

    @NotBlank(message = "공백일 수 없습니다.")
    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDateTime;

    @NotBlank
    private String memo;

    private ScheduleUpdateRequest() {
    }

    public ScheduleUpdateRequest(final String title, final LocalDateTime startDateTime, final LocalDateTime endDateTime,
                                 final String memo) {
        this.title = title;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.memo = memo;
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
