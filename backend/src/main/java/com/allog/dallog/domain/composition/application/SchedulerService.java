package com.allog.dallog.domain.composition.application;

import com.allog.dallog.domain.integrationschedule.domain.IntegrationSchedule;
import com.allog.dallog.domain.integrationschedule.domain.Period;
import com.allog.dallog.domain.member.application.MemberService;
import com.allog.dallog.domain.member.dto.MemberResponse;
import com.allog.dallog.domain.schedule.domain.scheduler.Scheduler;
import com.allog.dallog.domain.schedule.dto.request.DateRangeRequest;
import com.allog.dallog.domain.schedule.dto.response.PeriodResponse;
import com.allog.dallog.domain.subscription.application.SubscriptionService;
import com.allog.dallog.domain.subscription.dto.response.SubscriptionResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class SchedulerService {

    private final CalendarService calendarService;
    private final SubscriptionService subscriptionService;
    private final MemberService memberService;

    public SchedulerService(final CalendarService calendarService, final SubscriptionService subscriptionService,
                            final MemberService memberService) {
        this.calendarService = calendarService;
        this.subscriptionService = subscriptionService;
        this.memberService = memberService;
    }

    public List<PeriodResponse> getAvailablePeriods(final Long categoryId, final DateRangeRequest dateRange) {
        List<Long> subscriberIds = getSubscriberIds(categoryId);
        List<IntegrationSchedule> schedules = calendarService.getSchedulesBySubscriberIds(subscriberIds, dateRange);

        Scheduler scheduler = new Scheduler(schedules, dateRange.getStartDateTime(), dateRange.getEndDateTime());

        return convertPeriodsToPeriodResponses(scheduler.getPeriods());
    }

    private List<Long> getSubscriberIds(final Long categoryId) {
        List<SubscriptionResponse> subscriptions = subscriptionService.findByCategoryId(categoryId);
        return subscriptions.stream()
                .map(subscriptionResponse -> memberService.findBySubscriptionId(subscriptionResponse.getId()))
                .map(MemberResponse::getId)
                .collect(Collectors.toList());
    }

    private List<PeriodResponse> convertPeriodsToPeriodResponses(final List<Period> periods) {
        return periods.stream()
                .map(PeriodResponse::new)
                .collect(Collectors.toList());
    }
}
