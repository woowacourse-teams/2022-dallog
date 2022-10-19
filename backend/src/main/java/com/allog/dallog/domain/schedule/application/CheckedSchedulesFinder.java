package com.allog.dallog.domain.schedule.application;

import com.allog.dallog.domain.auth.application.OAuthClient;
import com.allog.dallog.domain.auth.dto.response.OAuthAccessTokenResponse;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.ExternalCategoryDetail;
import com.allog.dallog.domain.externalcalendar.application.ExternalCalendarClient;
import com.allog.dallog.domain.schedule.domain.IntegrationSchedule;
import com.allog.dallog.domain.schedule.domain.TypedSchedules;
import com.allog.dallog.domain.schedule.dto.MaterialToFindSchedules;
import com.allog.dallog.domain.schedule.dto.request.DateRangeRequest;
import com.allog.dallog.domain.schedule.dto.response.IntegrationScheduleResponses;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CheckedSchedulesFinder {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private final ScheduleService scheduleService;
    private final OAuthClient oAuthClient;
    private final ExternalCalendarClient externalCalendarClient;

    public CheckedSchedulesFinder(final ScheduleService scheduleService, final OAuthClient oAuthClient,
                                  final ExternalCalendarClient externalCalendarClient) {
        this.scheduleService = scheduleService;
        this.oAuthClient = oAuthClient;
        this.externalCalendarClient = externalCalendarClient;
    }

    public IntegrationScheduleResponses findMyCheckedSchedules(final Long memberId, final DateRangeRequest request) {
        MaterialToFindSchedules material = scheduleService.findInternalByMemberIdAndDateRange(memberId, request);

        List<IntegrationSchedule> schedules = material.getSchedules();

        String refreshToken = material.getRefreshToken();
        String accessToken = toAccessToken(refreshToken);

        List<IntegrationSchedule> externalSchedules = toExternalSchedules(request, material, accessToken);
        schedules.addAll(externalSchedules);

        return new IntegrationScheduleResponses(material.getSubscriptions(), new TypedSchedules(schedules));
    }

    private String toAccessToken(final String refreshToken) {
        OAuthAccessTokenResponse oAuthToken = oAuthClient.getAccessToken(refreshToken);
        return oAuthToken.getAccessToken();
    }

    private List<IntegrationSchedule> toExternalSchedules(final DateRangeRequest request,
                                                          final MaterialToFindSchedules material,
                                                          final String accessToken) {
        List<ExternalCategoryDetail> externalCategoryDetails = material.getExternalCategoryDetails();
        if (externalCategoryDetails.isEmpty()) {
            return new ArrayList<>();
        }

        return externalCategoryDetails.stream()
                .map(externalCategoryDetail -> findExternalSchedules(request, accessToken, externalCategoryDetail))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<IntegrationSchedule> findExternalSchedules(final DateRangeRequest request, final String accessToken,
                                                            final ExternalCategoryDetail externalCategoryDetail) {
        String startDateTime = request.getStartDateTime().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        String endDateTime = request.getEndDateTime().format(DateTimeFormatter.ofPattern(DATE_FORMAT));

        Category externalCategory = externalCategoryDetail.getCategory();
        String externalId = externalCategoryDetail.getExternalId();

        return externalCalendarClient.getExternalCalendarSchedules(
                accessToken, externalCategory.getId(), externalId, startDateTime, endDateTime);
    }
}
