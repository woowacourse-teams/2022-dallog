package com.allog.dallog.domain.composition.application;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.schedule.application.ScheduleService;
import com.allog.dallog.domain.schedule.domain.Schedule;
import com.allog.dallog.domain.schedule.domain.schedules.TypedSchedules;
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

    private final ScheduleService scheduleService;
    private final SubscriptionService subscriptionService;

    public CalendarService(final ScheduleService scheduleService, final SubscriptionService subscriptionService) {
        this.scheduleService = scheduleService;
        this.subscriptionService = subscriptionService;
    }

    public MemberScheduleResponses findSchedulesByMemberId(final Long memberId,
                                                           final DateRangeRequest dateRangeRequest) {
        List<Subscription> subscriptions = subscriptionService.getAllByMemberId(memberId);
        List<Category> categories = findCheckedCategoriesBy(subscriptions);
        List<Schedule> schedules = scheduleService.findBy(categories, dateRangeRequest);

        TypedSchedules typedSchedules = new TypedSchedules(schedules);
        return new MemberScheduleResponses(subscriptions, typedSchedules);
    }

    private List<Category> findCheckedCategoriesBy(final List<Subscription> subscriptions) {
        return subscriptions.stream()
                .filter(Subscription::isChecked)
                .map(Subscription::getCategory)
                .collect(Collectors.toList());
    }
}
