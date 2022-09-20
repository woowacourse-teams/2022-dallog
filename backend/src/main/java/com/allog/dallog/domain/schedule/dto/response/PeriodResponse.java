package com.allog.dallog.domain.schedule.dto.response;

import com.allog.dallog.domain.schedule.domain.Period;
import java.time.LocalDateTime;

public class PeriodResponse {

    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    public PeriodResponse(final Period period) {
        this.startDateTime = period.getStartDateTime();
        this.endDateTime = period.getEndDateTime();
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }
}
