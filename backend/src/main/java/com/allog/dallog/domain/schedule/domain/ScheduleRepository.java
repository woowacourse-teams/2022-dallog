package com.allog.dallog.domain.schedule.domain;

import com.allog.dallog.domain.schedule.exception.NoSuchScheduleException;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    void deleteByCategoryIdIn(final List<Long> categoryIds);

    default Schedule getById(final Long id) {
        return this.findById(id)
                .orElseThrow(NoSuchScheduleException::new);
    }
}
