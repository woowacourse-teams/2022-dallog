package com.allog.dallog.domain.composition.application;

import com.allog.dallog.domain.category.application.CategoryService;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class SchedulingService {

    private final CategoryService categoryService;
    private final ScheduleService scheduleService;
    private final SubscriptionService subscriptionService;
    private final MemberService memberService;

    public SchedulingService(final CategoryService categoryService, final ScheduleService scheduleService,
                             final SubscriptionService subscriptionService, final MemberService memberService) {
        this.categoryService = categoryService;
        this.scheduleService = scheduleService;
        this.subscriptionService = subscriptionService;
        this.memberService = memberService;
    }

    public List<PeriodResponse> getAvailablePeriods(final Long categoryId, final LocalDate startDate,
                                                    final LocalDate endDate) {
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

        List<Schedule> schedules = scheduleService.findBy(categories,
                DateRangeRequest.of(LocalDateTime.of(startDate, LocalTime.MIN),
                        LocalDateTime.of(endDate, LocalTime.MAX)));

        Scheduler scheduler = new Scheduler(schedules, startDate, endDate);
        return scheduler.getPeriods().stream().map(PeriodResponse::new).collect(Collectors.toList());
    }
}
