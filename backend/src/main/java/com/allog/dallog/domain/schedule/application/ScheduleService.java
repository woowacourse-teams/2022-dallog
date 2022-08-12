package com.allog.dallog.domain.schedule.application;

import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.schedule.domain.Schedule;
import com.allog.dallog.domain.schedule.domain.ScheduleRepository;
import com.allog.dallog.domain.schedule.domain.schedules.TypedSchedules;
import com.allog.dallog.domain.schedule.dto.request.DateRangeRequest;
import com.allog.dallog.domain.schedule.dto.request.ScheduleCreateRequest;
import com.allog.dallog.domain.schedule.dto.request.ScheduleUpdateRequest;
import com.allog.dallog.domain.schedule.dto.response.MemberScheduleResponses;
import com.allog.dallog.domain.schedule.dto.response.ScheduleResponse;
import com.allog.dallog.domain.schedule.exception.NoSuchScheduleException;
import com.allog.dallog.domain.subscription.application.SubscriptionService;
import com.allog.dallog.domain.subscription.domain.Subscription;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CategoryService categoryService;
    private final SubscriptionService subscriptionService;

    public ScheduleService(final ScheduleRepository scheduleRepository, final CategoryService categoryService,
                           final SubscriptionService subscriptionService) {
        this.scheduleRepository = scheduleRepository;
        this.categoryService = categoryService;
        this.subscriptionService = subscriptionService;
    }

    @Transactional
    public ScheduleResponse save(final Long memberId, final Long categoryId, final ScheduleCreateRequest request) {
        Category category = categoryService.getCategory(categoryId);
        categoryService.validateCreatorBy(memberId, category);
        Schedule schedule = scheduleRepository.save(request.toEntity(category));
        return new ScheduleResponse(schedule);
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

    public MemberScheduleResponses findSchedulesByMemberId(final Long memberId,
                                                           final DateRangeRequest dateRangeRequest) {
        List<Subscription> subscriptions = subscriptionService.getAllByMemberId(memberId);
        List<Category> categories = findCheckedCategoriesBy(subscriptions);
        List<Schedule> schedules = findSchedulesBy(categories, dateRangeRequest);
        TypedSchedules typedSchedules = new TypedSchedules(schedules);
        return new MemberScheduleResponses(subscriptions, typedSchedules);
    }

    private List<Category> findCheckedCategoriesBy(final List<Subscription> subscriptions) {
        return subscriptions.stream()
                .filter(Subscription::isChecked)
                .map(Subscription::getCategory)
                .collect(Collectors.toList());
    }

    private List<Schedule> findSchedulesBy(final List<Category> categories,
                                           final DateRangeRequest dateRangeRequest) {
        LocalDateTime startDate = dateRangeRequest.getStartDateTime();
        LocalDateTime endDate = dateRangeRequest.getEndDateTime();

        return categories.stream()
                .flatMap(category ->
                        scheduleRepository.findByCategoryIdAndBetween(category, startDate, endDate).stream())
                .collect(Collectors.toList());
    }
}
