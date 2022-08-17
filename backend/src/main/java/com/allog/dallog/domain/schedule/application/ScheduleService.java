package com.allog.dallog.domain.schedule.application;

import com.allog.dallog.domain.auth.exception.NoPermissionException;
import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.schedule.domain.Schedule;
import com.allog.dallog.domain.schedule.domain.ScheduleRepository;
import com.allog.dallog.domain.schedule.dto.request.ScheduleCreateRequest;
import com.allog.dallog.domain.schedule.dto.request.ScheduleUpdateRequest;
import com.allog.dallog.domain.schedule.dto.response.ScheduleResponse;
import com.allog.dallog.domain.schedule.exception.NoSuchScheduleException;
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
    public ScheduleResponse save(final Long memberId, final Long categoryId, final ScheduleCreateRequest request) {
        Category category = categoryService.getCategory(categoryId);
        categoryService.validateCreatorBy(memberId, category);
        validateExternalType(category);

        Schedule schedule = scheduleRepository.save(request.toEntity(category));
        return new ScheduleResponse(schedule);
    }

    private static void validateExternalType(final Category category) {
        if (category.isExternal()) {
            throw new NoPermissionException("외부 연동 카테고리에는 일정을 추가할 수 없습니다.");
        }
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
}
