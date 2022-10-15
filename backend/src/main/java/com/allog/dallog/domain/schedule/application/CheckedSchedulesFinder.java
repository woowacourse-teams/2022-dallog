package com.allog.dallog.domain.schedule.application;

import com.allog.dallog.domain.schedule.domain.IntegrationSchedule;
import com.allog.dallog.domain.schedule.domain.TypedSchedules;
import com.allog.dallog.domain.schedule.dto.MaterialToFindSchedules;
import com.allog.dallog.domain.schedule.dto.request.DateRangeRequest;
import com.allog.dallog.domain.schedule.dto.response.IntegrationScheduleResponses;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CheckedSchedulesFinder {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private final ScheduleService scheduleService;

    public CheckedSchedulesFinder(final ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    public IntegrationScheduleResponses findMyCheckedSchedules(final Long memberId, final DateRangeRequest request) {
        MaterialToFindSchedules material = scheduleService.findInternalByMemberIdAndDateRange(memberId, request);
        List<IntegrationSchedule> schedules = material.getSchedules();
        return new IntegrationScheduleResponses(material.getSubscriptions(), new TypedSchedules(schedules));
    }
}
