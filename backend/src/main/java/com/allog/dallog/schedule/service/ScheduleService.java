package com.allog.dallog.schedule.service;

import com.allog.dallog.schedule.domain.Schedule;
import com.allog.dallog.schedule.domain.ScheduleRepository;
import com.allog.dallog.schedule.dto.request.ScheduleCreateRequest;
import com.allog.dallog.schedule.dto.response.ScheduleResponse;
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

    public ScheduleService(final ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional
    public Long save(final ScheduleCreateRequest request) {
        Schedule schedule = scheduleRepository.save(request.toEntity());
        return schedule.getId();
    }

    public List<ScheduleResponse> findByYearAndMonth(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusDays(startDate.lengthOfMonth());

        List<Schedule> schedules = scheduleRepository.findByBetween(LocalDateTime.of(startDate, LocalTime.MIN),
                LocalDateTime.of(endDate, LocalTime.MIN));

        return schedules.stream()
                .map(ScheduleResponse::new)
                .collect(Collectors.toList());
    }
}
