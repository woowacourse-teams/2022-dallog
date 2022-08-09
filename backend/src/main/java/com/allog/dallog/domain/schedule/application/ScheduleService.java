package com.allog.dallog.domain.schedule.application;

import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.schedule.domain.Schedule;
import com.allog.dallog.domain.schedule.domain.ScheduleRepository;
import com.allog.dallog.domain.schedule.domain.repeat.ScheduleRepeatGroup;
import com.allog.dallog.domain.schedule.domain.repeat.ScheduleRepeatGroupRepository;
import com.allog.dallog.domain.schedule.domain.repeat.strategy.RepeatType;
import com.allog.dallog.domain.schedule.domain.schedules.TypedSchedules;
import com.allog.dallog.domain.schedule.dto.request.DateRangeRequest;
import com.allog.dallog.domain.schedule.dto.request.ScheduleCreateRequest;
import com.allog.dallog.domain.schedule.dto.request.ScheduleRepeatCreateRequest;
import com.allog.dallog.domain.schedule.dto.request.ScheduleUpdateRequest;
import com.allog.dallog.domain.schedule.dto.response.MemberScheduleResponses;
import com.allog.dallog.domain.schedule.dto.response.ScheduleResponse;
import com.allog.dallog.domain.schedule.exception.NoSuchScheduleException;
import com.allog.dallog.domain.subscription.application.SubscriptionService;
import com.allog.dallog.domain.subscription.domain.Subscription;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleRepeatGroupRepository scheduleRepeatGroupRepository;
    private final CategoryService categoryService;
    private final SubscriptionService subscriptionService;

    public ScheduleService(final ScheduleRepository scheduleRepository,
                           final ScheduleRepeatGroupRepository scheduleRepeatGroupRepository,
                           final CategoryService categoryService, final SubscriptionService subscriptionService) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleRepeatGroupRepository = scheduleRepeatGroupRepository;
        this.categoryService = categoryService;
        this.subscriptionService = subscriptionService;
    }

    @Transactional
    public Long save(final Long memberId, final Long categoryId, final ScheduleCreateRequest request) {
        Category category = categoryService.getCategory(categoryId);
        categoryService.validateCreatorBy(memberId, category);
        Schedule schedule = scheduleRepository.save(request.toEntity(category));
        return schedule.getId();
    }

    @Transactional
    public Long createRepeat(final Long memberId, final Long categoryId,
                             final ScheduleRepeatCreateRequest request) {
        Category category = categoryService.getCategory(categoryId);
        categoryService.validateCreatorBy(memberId, category);

        Schedule initialSchedule = request.toScheduleEntity(category);
        RepeatType repeatType = RepeatType.from(request.getRepeatType());
        ScheduleRepeatGroup scheduleRepeatGroup = scheduleRepeatGroupRepository.save(
                new ScheduleRepeatGroup(initialSchedule, request.getEndDate(), repeatType));
        return scheduleRepeatGroup.getId();
    }

    public ScheduleResponse findById(final Long id) {
        Schedule schedule = getSchedule(id);

        return new ScheduleResponse(schedule);
    }

    private Schedule getSchedule(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(NoSuchScheduleException::new);
    }

    @Transactional
    public void update(final Long id, final Long memberId, final ScheduleUpdateRequest request) {
        Schedule schedule = getSchedule(id);
        categoryService.validateCreatorBy(memberId, schedule.getCategory());
        schedule.change(request.getTitle(), request.getStartDateTime(), request.getEndDateTime(), request.getMemo());
    }

    @Transactional
    public void deleteById(final Long id, final Long memberId) {
        Schedule schedule = getSchedule(id);

        categoryService.validateCreatorBy(memberId, schedule.getCategory());
        scheduleRepository.deleteById(id);
    }

    @Transactional
    public void deleteByIdWithAfterAllSchedules(final Long id, final Long memberId) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(NoSuchScheduleException::new);
        categoryService.validateCreatorBy(memberId, schedule.getCategory());

        ScheduleRepeatGroup scheduleRepeatGroup = schedule.getScheduleRepeatGroup();
        scheduleRepeatGroup.deleteWithAfterAllSchedules(schedule);
    }

    public MemberScheduleResponses findSchedulesByMemberId(final Long memberId,
                                                           final DateRangeRequest dateRangeRequest) {
        // TODO: 리팩토링 및 성능개선
        List<Subscription> subscriptions = subscriptionService.getAllByMemberId(memberId);
        List<Schedule> schedules = getSchedulesFrom(subscriptions, dateRangeRequest);
        TypedSchedules typedSchedules = new TypedSchedules(schedules);
        return new MemberScheduleResponses(subscriptions, typedSchedules);
    }

    private List<Schedule> getSchedulesFrom(final List<Subscription> subscriptions,
                                            final DateRangeRequest dateRangeRequest) {
        LocalDate startDate = dateRangeRequest.getStartDate();
        LocalDate endDate = dateRangeRequest.getEndDate();

        return subscriptions.stream()
                .filter(Subscription::isChecked) // 체크된 구독 필터
                .map(Subscription::getCategory)
                .flatMap(category -> category.getSchedules().stream())
                .filter(schedule -> schedule.isBetween(startDate, endDate))
                .collect(Collectors.toList());
    }
}
