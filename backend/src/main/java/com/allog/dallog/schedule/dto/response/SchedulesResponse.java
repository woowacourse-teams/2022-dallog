package com.allog.dallog.schedule.dto.response;

import java.util.List;

public class SchedulesResponse {

    private List<ScheduleResponse> data;

    public SchedulesResponse() {
    }

    public SchedulesResponse(final List<ScheduleResponse> data) {
        this.data = data;
    }

    public List<ScheduleResponse> getData() {
        return data;
    }
}
