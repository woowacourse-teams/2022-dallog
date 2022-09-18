package com.allog.dallog.domain.schedule.application;

import com.allog.dallog.domain.auth.application.OAuthClient;
import com.allog.dallog.domain.auth.domain.OAuthToken;
import com.allog.dallog.domain.auth.domain.OAuthTokenRepository;
import com.allog.dallog.domain.auth.dto.response.OAuthAccessTokenResponse;
import com.allog.dallog.domain.category.application.ExternalCategoryDetailService;
import com.allog.dallog.domain.category.domain.ExternalCategoryDetail;
import com.allog.dallog.domain.externalcalendar.application.ExternalCalendarClient;
import com.allog.dallog.domain.integrationschedule.domain.IntegrationSchedule;
import com.allog.dallog.domain.integrationschedule.domain.TypedSchedules;
import com.allog.dallog.domain.schedule.dto.request.DateRangeRequest;
import com.allog.dallog.domain.schedule.dto.response.MemberScheduleResponses;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import com.allog.dallog.domain.subscription.domain.Subscriptions;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SubscribingSchedulesFinder {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private final ScheduleService scheduleService;
    private final SubscriptionRepository subscriptionRepository;
    private final ExternalCategoryDetailService externalCategoryDetailService;
    private final ExternalCalendarClient externalCalendarClient;
    private final OAuthTokenRepository oAuthTokenRepository;
    private final OAuthClient oAuthClient;

    public SubscribingSchedulesFinder(final ScheduleService scheduleService,
                                      final SubscriptionRepository subscriptionRepository,
                                      final ExternalCategoryDetailService externalCategoryDetailService,
                                      final ExternalCalendarClient externalCalendarClient,
                                      final OAuthTokenRepository oAuthTokenRepository, final OAuthClient oAuthClient) {
        this.scheduleService = scheduleService;
        this.subscriptionRepository = subscriptionRepository;
        this.externalCategoryDetailService = externalCategoryDetailService;
        this.externalCalendarClient = externalCalendarClient;
        this.oAuthTokenRepository = oAuthTokenRepository;
        this.oAuthClient = oAuthClient;
    }

    public MemberScheduleResponses findMySubscribingSchedules(final Long memberId, final DateRangeRequest dateRange) {
        List<IntegrationSchedule> schedules = new ArrayList<>();

        List<IntegrationSchedule> externalSchedules = findExternalSchedules(memberId, dateRange);
        List<IntegrationSchedule> internalSchedules = scheduleService.findInternalByMemberIdAndDateRange(memberId,
                dateRange);

        schedules.addAll(externalSchedules);
        schedules.addAll(internalSchedules);

        Subscriptions subscriptions = new Subscriptions(subscriptionRepository.findByMemberId(memberId));
        return new MemberScheduleResponses(subscriptions, new TypedSchedules(schedules));
    }

    private List<IntegrationSchedule> findExternalSchedules(final Long memberId, final DateRangeRequest dateRange) {
        List<IntegrationSchedule> schedules = new ArrayList<>();

        List<ExternalCategoryDetail> details = externalCategoryDetailService.findByMemberId(memberId);
        if (details.isEmpty()) {
            return schedules;
        }

        String accessToken = getGoogleAccessToken(memberId);
        for (ExternalCategoryDetail detail : details) {
            List<IntegrationSchedule> googleSchedules = findGoogleSchedules(dateRange, accessToken, detail);
            schedules.addAll(googleSchedules);
        }
        return schedules;
    }

    private String getGoogleAccessToken(final Long memberId) {
        OAuthToken oAuthToken = oAuthTokenRepository.getByMemberId(memberId);
        String refreshToken = oAuthToken.getRefreshToken();

        OAuthAccessTokenResponse accessTokenResponse = oAuthClient.getAccessToken(refreshToken);
        return accessTokenResponse.getValue();
    }

    private List<IntegrationSchedule> findGoogleSchedules(final DateRangeRequest dateRange, final String accessToken,
                                                          final ExternalCategoryDetail detail) {
        LocalDateTime startDateTime = dateRange.getStartDateTime();
        LocalDateTime endDateTime = dateRange.getEndDateTime();

        String startTime = startDateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        String endTime = endDateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT));

        return externalCalendarClient.getExternalCalendarSchedules(
                accessToken, detail.getCategory().getId(), detail.getExternalId(), startTime, endTime);
    }
}
