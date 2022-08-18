package com.allog.dallog.domain.composition.application;

import com.allog.dallog.domain.auth.application.OAuthClient;
import com.allog.dallog.domain.auth.domain.OAuthToken;
import com.allog.dallog.domain.auth.domain.OAuthTokenRepository;
import com.allog.dallog.domain.auth.exception.NoSuchOAuthTokenException;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.ExternalCategoryDetail;
import com.allog.dallog.domain.category.domain.ExternalCategoryDetailRepository;
import com.allog.dallog.domain.category.exception.NoSuchExternalCategoryDetailException;
import com.allog.dallog.domain.externalcalendar.application.ExternalCalendarClient;
import com.allog.dallog.domain.integrationschedule.dao.IntegrationScheduleDao;
import com.allog.dallog.domain.integrationschedule.domain.IntegrationSchedule;
import com.allog.dallog.domain.integrationschedule.domain.TypedSchedules;
import com.allog.dallog.domain.schedule.dto.request.DateRangeRequest;
import com.allog.dallog.domain.schedule.dto.response.MemberScheduleResponses;
import com.allog.dallog.domain.subscription.application.SubscriptionService;
import com.allog.dallog.domain.subscription.domain.Subscription;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// TODO: 전체 리팩토링
@Transactional(readOnly = true)
@Service
public class CalendarService {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private final SubscriptionService subscriptionService;
    private final IntegrationScheduleDao integrationScheduleDao;
    private final ExternalCategoryDetailRepository externalCategoryDetailRepository;
    private final OAuthTokenRepository oAuthTokenRepository;
    private final OAuthClient oAuthClient;
    private final ExternalCalendarClient externalCalendarClient;

    public CalendarService(final SubscriptionService subscriptionService,
                           final IntegrationScheduleDao integrationScheduleDao,
                           final ExternalCategoryDetailRepository externalCategoryDetailRepository,
                           final OAuthTokenRepository oAuthTokenRepository, final OAuthClient oAuthClient,
                           final ExternalCalendarClient externalCalendarClient) {
        this.subscriptionService = subscriptionService;
        this.integrationScheduleDao = integrationScheduleDao;
        this.externalCategoryDetailRepository = externalCategoryDetailRepository;
        this.oAuthTokenRepository = oAuthTokenRepository;
        this.oAuthClient = oAuthClient;
        this.externalCalendarClient = externalCalendarClient;
    }

    public List<IntegrationSchedule> getSchedulesOfSubscriberIds(final List<Long> subscriberIds,
                                                                 final DateRangeRequest dateRange) {
        return subscriberIds.stream()
                .flatMap(subscriberId -> getIntegrationSchedulesByMemberIdAndSubscriptions(subscriberId,
                        dateRange).stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public MemberScheduleResponses findSchedulesByMemberId(final Long memberId,
                                                           final DateRangeRequest dateRange) {
        List<Subscription> subscriptions = subscriptionService.getAllByMemberId(memberId);
        List<IntegrationSchedule> integrationSchedules = getIntegrationSchedulesByMemberIdAndSubscriptions(memberId,
                dateRange);
        return new MemberScheduleResponses(subscriptions, new TypedSchedules(integrationSchedules));
    }

    private List<IntegrationSchedule> getIntegrationSchedulesByMemberIdAndSubscriptions(final Long memberId,
                                                                                        final DateRangeRequest dateRange) {
        List<Subscription> subscriptions = subscriptionService.getAllByMemberId(memberId);
        List<Long> internalCategoryIds = findCheckedInternalCategories(subscriptions);

        List<IntegrationSchedule> integrationSchedules = new ArrayList<>(
                integrationScheduleDao.findByCategoryIdInAndBetween(
                        internalCategoryIds, dateRange.getStartDateTime(), dateRange.getEndDateTime()));

        List<ExternalCategoryDetail> externalCategoryDetails = findCheckedExternalCategoryDetails(subscriptions);
        if (!externalCategoryDetails.isEmpty()) {
            integrationSchedules.addAll(getExternalSchedules(memberId, dateRange, externalCategoryDetails));
        }

        return integrationSchedules;
    }

    private List<Long> findCheckedInternalCategories(final List<Subscription> subscriptions) {
        return findCheckedSubscriptions(subscriptions)
                .stream()
                .map(Subscription::getCategory)
                .filter(Category::isInternal)
                .map(Category::getId)
                .collect(Collectors.toList());
    }

    private List<ExternalCategoryDetail> findCheckedExternalCategoryDetails(final List<Subscription> subscriptions) {
        return findCheckedSubscriptions(subscriptions)
                .stream()
                .map(Subscription::getCategory)
                .filter(Category::isExternal)
                .map(this::getExternalCategoryDetail)
                .collect(Collectors.toList());
    }

    private List<Subscription> findCheckedSubscriptions(final List<Subscription> subscriptions) {
        return subscriptions.stream()
                .filter(Subscription::isChecked)
                .collect(Collectors.toList());
    }

    private ExternalCategoryDetail getExternalCategoryDetail(final Category category) {
        return externalCategoryDetailRepository.findByCategoryId(category.getId())
                .orElseThrow(NoSuchExternalCategoryDetailException::new);
    }

    private List<IntegrationSchedule> getExternalSchedules(final Long memberId, final DateRangeRequest request,
                                                           final List<ExternalCategoryDetail> externalCategories) {
        String refreshToken = getOAuthToken(memberId).getRefreshToken();
        String accessToken = oAuthClient.getAccessToken(refreshToken).getAccessToken();

        return externalCategories.stream()
                .flatMap(externalCategory -> flatIntegrationSchedules(request, accessToken, externalCategory))
                .collect(Collectors.toList());
    }

    private OAuthToken getOAuthToken(final Long memberId) {
        return oAuthTokenRepository.findByMemberId(memberId).orElseThrow(NoSuchOAuthTokenException::new);
    }

    private Stream<IntegrationSchedule> flatIntegrationSchedules(final DateRangeRequest dateRangeRequest,
                                                                 final String accessToken,
                                                                 final ExternalCategoryDetail externalCategory) {
        String startDateTime = dateRangeRequest.getStartDateTime().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        String endDateTime = dateRangeRequest.getEndDateTime().format(DateTimeFormatter.ofPattern(DATE_FORMAT));

        return externalCalendarClient.getExternalCalendarSchedules(accessToken, externalCategory.getCategory().getId(),
                externalCategory.getExternalId(), startDateTime, endDateTime).stream();
    }
}
