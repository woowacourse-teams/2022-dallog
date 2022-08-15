package com.allog.dallog.domain.composition.application;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.integrationschedule.dao.IntegrationScheduleDao;
import com.allog.dallog.domain.integrationschedule.domain.IntegrationSchedule;
import com.allog.dallog.domain.integrationschedule.domain.TypedSchedules;
import com.allog.dallog.domain.schedule.dto.request.DateRangeRequest;
import com.allog.dallog.domain.schedule.dto.response.MemberScheduleResponses;
import com.allog.dallog.domain.subscription.application.SubscriptionService;
import com.allog.dallog.domain.subscription.domain.Subscription;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class CalendarService {

    private final SubscriptionService subscriptionService;
    private final IntegrationScheduleDao integrationScheduleDao;

    public CalendarService(final SubscriptionService subscriptionService,
                           final IntegrationScheduleDao integrationScheduleDao) {
        this.subscriptionService = subscriptionService;
        this.integrationScheduleDao = integrationScheduleDao;
    }

    public MemberScheduleResponses findSchedulesByMemberId(final Long memberId,
                                                           final DateRangeRequest dateRangeRequest) {
        List<Subscription> subscriptions = subscriptionService.getAllByMemberId(memberId);
        List<Long> categoryIds = findCheckedCategoriesBy(subscriptions)
                .stream()
                .map(Category::getId)
                .collect(Collectors.toList());

        List<IntegrationSchedule> integrationSchedules = integrationScheduleDao.findByCategoryIdInAndBetween(
                categoryIds, dateRangeRequest.getStartDateTime(), dateRangeRequest.getEndDateTime());

        TypedSchedules typedSchedules = new TypedSchedules(integrationSchedules);
        return new MemberScheduleResponses(subscriptions, typedSchedules);
    }

    private List<Category> findCheckedCategoriesBy(final List<Subscription> subscriptions) {
        return subscriptions.stream()
                .filter(Subscription::isChecked)
                .map(Subscription::getCategory)
                .collect(Collectors.toList());
    }
}
