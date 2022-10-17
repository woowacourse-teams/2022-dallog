package com.allog.dallog.domain.schedule.application;

import com.allog.dallog.domain.externalcalendar.application.ExternalCalendarClient;
import com.allog.dallog.domain.schedule.domain.IntegrationSchedule;
import com.allog.dallog.domain.schedule.domain.TypedSchedules;
import com.allog.dallog.domain.schedule.dto.MaterialToFindSchedules;
import com.allog.dallog.domain.schedule.dto.request.DateRangeRequest;
import com.allog.dallog.domain.schedule.dto.response.IntegrationScheduleResponses;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CheckedSchedulesFinder {

    private final ScheduleService scheduleService;
    private final ExternalCalendarClient externalCalendarClient;

    public CheckedSchedulesFinder(final ScheduleService scheduleService,
                                  final ExternalCalendarClient externalCalendarClient) {
        this.scheduleService = scheduleService;
        this.externalCalendarClient = externalCalendarClient;
    }

    @Transactional
    public IntegrationScheduleResponses findMyCheckedSchedules(final Long memberId, final DateRangeRequest request) {
        MaterialToFindSchedules material = scheduleService.findInternalByMemberIdAndDateRange(memberId, request);

        List<IntegrationSchedule> schedules = material.getSchedules();

        List<IntegrationSchedule> externalSchedules = externalCalendarClient.getExternalCalendarSchedules(null, null,
                null, null, null);
        schedules.addAll(externalSchedules);

        return new IntegrationScheduleResponses(material.getSubscriptions(), new TypedSchedules(schedules));
    }
}
