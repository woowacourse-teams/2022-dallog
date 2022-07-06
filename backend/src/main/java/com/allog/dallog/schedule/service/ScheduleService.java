package com.allog.dallog.schedule.service;

import com.allog.dallog.schedule.domain.Schedule;
import com.allog.dallog.schedule.domain.ScheduleRepository;
import com.allog.dallog.schedule.dto.request.ScheduleCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(final ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional
    public Long save(final ScheduleCreateRequest request) {
        Schedule schedule = scheduleRepository.save(request.toEntity());
        return schedule.getId();
    }
}
