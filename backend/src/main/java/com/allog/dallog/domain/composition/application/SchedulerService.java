package com.allog.dallog.domain.composition.application;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.member.application.MemberService;
import com.allog.dallog.domain.member.dto.MemberResponse;
import com.allog.dallog.domain.schedule.application.ScheduleService;
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
    // TODO: 이름 수정

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
        // TODO: 리팩토링
        List<SubscriptionResponse> subscriptions = subscriptionService.findByCategoryId(categoryId);
        List<MemberResponse> members = subscriptions.stream()
                .map(subscriptionResponse -> memberService.findBySubscriptionId(subscriptionResponse.getId()))
                .collect(Collectors.toList());

        List<Category> categories = members.stream()
                .flatMap(memberResponse -> subscriptionService.getAllByMemberId(memberResponse.getId()).stream())
                .filter(Subscription::isChecked)
                .map(Subscription::getCategory)
                .collect(Collectors.toList());

        List<Schedule> schedules = scheduleService.findBy(categories, dateRange);

        Scheduler scheduler = new Scheduler(schedules, dateRange.getStartDateTime().toLocalDate(),
                dateRange.getEndDateTime().toLocalDate());
        return scheduler.getPeriods().stream().map(PeriodResponse::new).collect(Collectors.toList());
    }
}
