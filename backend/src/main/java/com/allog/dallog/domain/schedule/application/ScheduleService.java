package com.allog.dallog.domain.schedule.application;

import com.allog.dallog.domain.auth.exception.NoPermissionException;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.schedule.domain.Schedule;
import com.allog.dallog.domain.schedule.domain.ScheduleRepository;
import com.allog.dallog.domain.schedule.dto.request.ScheduleCreateRequest;
import com.allog.dallog.domain.schedule.dto.response.ScheduleResponse;
import com.allog.dallog.domain.schedule.exception.NoSuchScheduleException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CategoryService categoryService;

    public ScheduleService(final ScheduleRepository scheduleRepository, final CategoryService categoryService) {
        this.scheduleRepository = scheduleRepository;
        this.categoryService = categoryService;
    }

    @Transactional
    public Long save(final Long memberId, final Long categoryId, final ScheduleCreateRequest request) {
        Category category = categoryService.getCategory(categoryId);
        validateCategoryPermission(memberId, category);
        Schedule schedule = scheduleRepository.save(request.toEntity(category));
        return schedule.getId();
    }

    public List<ScheduleResponse> findByYearAndMonth(final int year, final int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusDays(startDate.lengthOfMonth());

        List<Schedule> schedules = scheduleRepository.findByBetween(LocalDateTime.of(startDate, LocalTime.MIN),
                LocalDateTime.of(endDate, LocalTime.MIN));

        return schedules.stream()
                .map(ScheduleResponse::new)
                .collect(Collectors.toList());
    }

    public ScheduleResponse findById(final Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(NoSuchScheduleException::new);

        return new ScheduleResponse(schedule);
    }

    @Transactional
    public void deleteById(final Long id, final Long memberId) {
        Category category = scheduleRepository.findById(id)
                .orElseThrow(NoSuchScheduleException::new)
                .getCategory();

        validateCategoryPermission(memberId, category);
        scheduleRepository.deleteById(id);
    }

    private void validateCategoryPermission(final Long memberId, final Category category) {
        if (!category.isCreator(memberId)) {
            throw new NoPermissionException();
        }
    }
}
