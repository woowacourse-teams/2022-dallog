package com.allog.dallog.domain.schedule.application;

import com.allog.dallog.domain.auth.application.OAuthClient;
import com.allog.dallog.domain.auth.domain.OAuthTokenRepository;
import com.allog.dallog.domain.externalcalendar.application.ExternalCalendarClient;
import com.allog.dallog.domain.schedule.domain.IntegrationSchedule;
import com.allog.dallog.domain.schedule.domain.scheduler.Scheduler;
import com.allog.dallog.domain.schedule.dto.AvailablePeriodMaterial;
import com.allog.dallog.domain.schedule.dto.ExternalRequestMaterial;
import com.allog.dallog.domain.schedule.dto.request.DateRangeRequest;
import com.allog.dallog.domain.schedule.dto.response.PeriodResponse;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class AvailablePeriodsFinder {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private final ScheduleService scheduleService;
    private final OAuthTokenRepository oAuthTokenRepository;
    private final OAuthClient oAuthClient;
    private final ExternalCalendarClient externalCalendarClient;

    public AvailablePeriodsFinder(final ScheduleService scheduleService,
                                  final OAuthTokenRepository oAuthTokenRepository,
                                  final OAuthClient oAuthClient, final ExternalCalendarClient externalCalendarClient) {
        this.scheduleService = scheduleService;
        this.oAuthTokenRepository = oAuthTokenRepository;
        this.oAuthClient = oAuthClient;
        this.externalCalendarClient = externalCalendarClient;
    }

    public List<PeriodResponse> getAvailablePeriods(final Long categoryId, final DateRangeRequest request) {
        AvailablePeriodMaterial material = scheduleService.findInMembersByCategoryIdAndDateRange(categoryId, request);

        String startDateTime = request.getStartDateTime().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        String endDateTime = request.getEndDateTime().format(DateTimeFormatter.ofPattern(DATE_FORMAT));

        List<IntegrationSchedule> schedules = material.getInternalSchedules();
        List<IntegrationSchedule> externalSchedules = toExternalSchedules(startDateTime, endDateTime, material);

        schedules.addAll(externalSchedules);
        Scheduler scheduler = new Scheduler(schedules, request.getStartDateTime(), request.getEndDateTime());

        return toPeriodResponses(scheduler);
    }

    private List<IntegrationSchedule> toExternalSchedules(final String startDateTime, final String endDateTime,
                                                          final AvailablePeriodMaterial material) {
        return material.getTokenByExternalIds()
                .entrySet()
                .stream()
                .map(entry -> toExternalSchedules(entry, startDateTime, endDateTime))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<IntegrationSchedule> toExternalSchedules(final Entry<String, List<ExternalRequestMaterial>> entry,
                                                          final String startDateTime, final String endDateTime) {
        String refreshToken = entry.getKey();
        List<ExternalRequestMaterial> externalRequestMaterials = entry.getValue();

        String accessToken = oAuthClient.getAccessToken(refreshToken).getAccessToken();

        return externalRequestMaterials.stream()
                .map(externalRequestMaterial ->
                        toExternalSchedules(accessToken, externalRequestMaterial, startDateTime, endDateTime))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<IntegrationSchedule> toExternalSchedules(final String accessToken,
                                                          final ExternalRequestMaterial externalRequestMaterial,
                                                          final String startDateTime, final String endDateTime) {
        Long internalCategoryId = externalRequestMaterial.getInternalCategoryId();
        String externalCalendarId = externalRequestMaterial.getExternalCalendarId();

        return externalCalendarClient.getExternalCalendarSchedules(
                accessToken, internalCategoryId, externalCalendarId, startDateTime, endDateTime);
    }

    private List<PeriodResponse> toPeriodResponses(final Scheduler scheduler) {
        return scheduler.getPeriods()
                .stream()
                .map(PeriodResponse::new)
                .collect(Collectors.toList());
    }
}
