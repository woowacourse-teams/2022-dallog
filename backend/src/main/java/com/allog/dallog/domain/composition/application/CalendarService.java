package com.allog.dallog.domain.composition.application;

import com.allog.dallog.domain.auth.application.OAuthClient;
import com.allog.dallog.domain.auth.domain.OAuthTokenRepository;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.ExternalCategoryDetail;
import com.allog.dallog.domain.category.domain.ExternalCategoryDetailRepository;
import com.allog.dallog.domain.externalcalendar.application.ExternalCalendarClient;
import com.allog.dallog.domain.integrationschedule.dao.IntegrationScheduleDao;
import com.allog.dallog.domain.integrationschedule.domain.IntegrationSchedule;
import com.allog.dallog.domain.integrationschedule.domain.TypedSchedules;
import com.allog.dallog.domain.schedule.dto.request.DateRangeRequest;
import com.allog.dallog.domain.schedule.dto.response.MemberScheduleResponses;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import com.allog.dallog.domain.subscription.domain.Subscriptions;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class CalendarService {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private final IntegrationScheduleDao integrationScheduleDao;
    private final SubscriptionRepository subscriptionRepository;
    private final ExternalCategoryDetailRepository externalCategoryDetailRepository;
    private final OAuthTokenRepository oAuthTokenRepository;
    private final OAuthClient oAuthClient;
    private final ExternalCalendarClient externalCalendarClient;

    public CalendarService(final IntegrationScheduleDao integrationScheduleDao,
                           final SubscriptionRepository subscriptionRepository,
                           final ExternalCategoryDetailRepository externalCategoryDetailRepository,
                           final OAuthTokenRepository oAuthTokenRepository, final OAuthClient oAuthClient,
                           final ExternalCalendarClient externalCalendarClient) {
        this.integrationScheduleDao = integrationScheduleDao;
        this.subscriptionRepository = subscriptionRepository;
        this.externalCategoryDetailRepository = externalCategoryDetailRepository;
        this.oAuthTokenRepository = oAuthTokenRepository;
        this.oAuthClient = oAuthClient;
        this.externalCalendarClient = externalCalendarClient;
    }

    public List<IntegrationSchedule> getSchedulesBySubscriberIds(final List<Long> subscriberIds,
                                                                 final DateRangeRequest dateRange) {
        return subscriberIds.stream()
                .flatMap(subscriberId -> getIntegrationSchedulesByMemberId(subscriberId, dateRange).stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public MemberScheduleResponses findSchedulesByMemberId(final Long memberId, final DateRangeRequest dateRange) {
        Subscriptions subscriptions = new Subscriptions(subscriptionRepository.findByMemberId(memberId));
        List<IntegrationSchedule> integrationSchedules = getIntegrationSchedulesByMemberId(memberId, dateRange);
        return new MemberScheduleResponses(subscriptions, new TypedSchedules(integrationSchedules));
    }

    private List<IntegrationSchedule> getIntegrationSchedulesByMemberId(final Long memberId,
                                                                        final DateRangeRequest dateRange) {
        Subscriptions subscriptions = new Subscriptions(subscriptionRepository.findByMemberId(memberId));
        List<Long> internalCategoryIds = subscriptions.findCheckedCategoryIdsBy(Category::isInternal);

        List<IntegrationSchedule> integrationSchedules = integrationScheduleDao.findByCategoryIdInAndBetween(
                internalCategoryIds, dateRange.getStartDateTime(), dateRange.getEndDateTime());

        List<ExternalCategoryDetail> externalCategoryDetails = findCheckedExternalCategoryDetails(subscriptions);
        if (!externalCategoryDetails.isEmpty()) {
            integrationSchedules.addAll(getExternalSchedules(memberId, dateRange, externalCategoryDetails));
        }

        return integrationSchedules;
    }

    private List<ExternalCategoryDetail> findCheckedExternalCategoryDetails(final Subscriptions subscriptions) {
        return subscriptions.findCheckedCategoryIdsBy(Category::isExternal)
                .stream()
                .map(externalCategoryDetailRepository::getByCategoryId)
                .collect(Collectors.toList());
    }

    private List<IntegrationSchedule> getExternalSchedules(final Long memberId, final DateRangeRequest request,
                                                           final List<ExternalCategoryDetail> externalCategories) {
        String refreshToken = oAuthTokenRepository.getByMemberId(memberId)
                .getRefreshToken();
        String accessToken = oAuthClient.getAccessToken(refreshToken)
                .getValue();

        return externalCategories.stream()
                .flatMap(externalCategory -> flatIntegrationSchedules(request, accessToken, externalCategory))
                .collect(Collectors.toList());
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
