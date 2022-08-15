package com.allog.dallog.domain.schedule.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    void deleteByCategoryIdIn(final List<Long> categoryIds);
}
