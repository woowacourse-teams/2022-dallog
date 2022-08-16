package com.allog.dallog.domain.composition.application;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.member.application.MemberService;
import com.allog.dallog.domain.member.dto.MemberResponse;
import com.allog.dallog.domain.schedule.application.ScheduleService;
import com.allog.dallog.domain.schedule.domain.Period;
import com.allog.dallog.domain.schedule.domain.Schedule;
import com.allog.dallog.domain.schedule.domain.scheduler.Scheduler;
import com.allog.dallog.domain.schedule.dto.request.DateRangeRequest;
import com.allog.dallog.domain.schedule.dto.response.PeriodResponse;
import com.allog.dallog.domain.subscription.application.SubscriptionService;
import com.allog.dallog.domain.subscription.domain.Subscription;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class SchedulerService {

    private final ScheduleService scheduleService;
    private final SubscriptionService subscriptionService;
    private final MemberService memberService;

    public SchedulerService(final ScheduleService scheduleService, final SubscriptionService subscriptionService,
                            final MemberService memberService) {
        this.scheduleService = scheduleService;
        this.subscriptionService = subscriptionService;
        this.memberService = memberService;
    }

    public List<PeriodResponse> getAvailablePeriods(final Long categoryId, final DateRangeRequest dateRange) {
        List<MemberResponse> subscribers = getSubscribers(categoryId);
        List<Category> categories = getCategoriesOfSubscribers(subscribers);
        List<Schedule> schedules = getSchedulesOfCategories(categories, dateRange);

        Scheduler scheduler = new Scheduler(schedules, dateRange.getStartDateTime().toLocalDate(),
                dateRange.getEndDateTime().toLocalDate());

        return convertPeriodsToPeriodResponses(scheduler.getPeriods());
    }

    private List<MemberResponse> getSubscribers(final Long categoryId) {
        List<SubscriptionResponse> subscriptions = subscriptionService.findByCategoryId(categoryId);
        return subscriptions.stream()
                .map(subscriptionResponse -> memberService.findBySubscriptionId(subscriptionResponse.getId()))
                .collect(Collectors.toList());
    }

    private List<Category> getCategoriesOfSubscribers(final List<MemberResponse> subscribers) {
        return subscribers.stream()
                .flatMap(memberResponse -> subscriptionService.getAllByMemberId(memberResponse.getId()).stream())
                .filter(Subscription::isChecked)
                .map(Subscription::getCategory)
                .collect(Collectors.toList());
    }

    private List<Schedule> getSchedulesOfCategories(final List<Category> categories, final DateRangeRequest dateRange) {
        return scheduleService.findBy(categories, dateRange);
    }

    private List<PeriodResponse> convertPeriodsToPeriodResponses(final List<Period> periods) {
        return periods.stream()
                .map(PeriodResponse::new)
                .collect(Collectors.toList());
    }
}
