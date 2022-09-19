package com.allog.dallog.domain.schedule.application;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.schedule.domain.IntegrationSchedule;
import com.allog.dallog.domain.schedule.domain.Schedule;
import com.allog.dallog.domain.schedule.domain.ScheduleRepository;
import com.allog.dallog.domain.schedule.dto.request.DateRangeRequest;
import com.allog.dallog.domain.schedule.dto.request.ScheduleCreateRequest;
import com.allog.dallog.domain.schedule.dto.request.ScheduleUpdateRequest;
import com.allog.dallog.domain.schedule.dto.response.ScheduleResponse;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import com.allog.dallog.domain.subscription.domain.Subscriptions;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final SubscriptionRepository subscriptionRepository;

    public ScheduleService(final ScheduleRepository scheduleRepository, final CategoryRepository categoryRepository,
                           final MemberRepository memberRepository,
                           final SubscriptionRepository subscriptionRepository) {
        this.scheduleRepository = scheduleRepository;
        this.categoryRepository = categoryRepository;
        this.memberRepository = memberRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Transactional
    public ScheduleResponse save(final Long memberId, final Long categoryId, final ScheduleCreateRequest request) {
        Category category = categoryRepository.getById(categoryId);
        Member member = memberRepository.getById(memberId);
        category.validateCanAddSchedule(member);
        Schedule schedule = scheduleRepository.save(request.toEntity(category));
        return new ScheduleResponse(schedule);
    }

    public ScheduleResponse findById(final Long id) {
        Schedule schedule = scheduleRepository.getById(id);
        return new ScheduleResponse(schedule);
    }

    public List<IntegrationSchedule> findInternalByMemberIdAndDateRange(final Long memberId,
                                                                        final DateRangeRequest dateRange) {
        Subscriptions subscriptions = new Subscriptions(subscriptionRepository.findByMemberId(memberId));
        return integrationSchedules(dateRange, subscriptions);
    }

    private List<IntegrationSchedule> integrationSchedules(final DateRangeRequest dateRange,
                                                           final Subscriptions subscriptions) {
        List<IntegrationSchedule> integrationSchedules = new ArrayList<>();

        List<Category> categories = subscriptions.findInternalCategory();
        for (Category category : categories) {
            List<IntegrationSchedule> schedules = scheduleRepository.createByCategoryAndBetween(
                    category, dateRange.getStartDateTime(), dateRange.getEndDateTime());
            integrationSchedules.addAll(schedules);
        }
        return integrationSchedules;
    }

    @Transactional
    public void update(final Long id, final Long memberId, final ScheduleUpdateRequest request) {
        Schedule schedule = scheduleRepository.getById(id);
        schedule.validateEditablePossible(memberId);
        schedule.change(request.getTitle(), request.getStartDateTime(), request.getEndDateTime(), request.getMemo());
    }

    @Transactional
    public void delete(final Long id, final Long memberId) {
        Schedule schedule = scheduleRepository.getById(id);
        schedule.validateEditablePossible(memberId);
        scheduleRepository.deleteById(id);
    }
}
