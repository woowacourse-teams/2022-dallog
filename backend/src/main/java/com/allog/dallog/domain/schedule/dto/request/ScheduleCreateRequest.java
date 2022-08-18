package com.allog.dallog.domain.schedule.dto.request;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.schedule.domain.Schedule;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

public class ScheduleCreateRequest {

    @NotNull(message = "Null일 수 없습니다.")
    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDateTime;

    @NotNull(message = "Null일 수 없습니다.")
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

    public Schedule toEntity(final Category category) {
        return new Schedule(category, title, startDateTime, endDateTime, memo);
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
