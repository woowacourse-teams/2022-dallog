package com.allog.dallog.schedule.dto.response;

import java.util.List;

public class SchedulesResponse {

    private List<ScheduleResponse> schedules;

    public SchedulesResponse() {
    }

    public SchedulesResponse(final List<ScheduleResponse> schedules) {
        this.schedules = schedules;
    }

    public List<ScheduleResponse> getSchedules() {
        return schedules;
    }
}
