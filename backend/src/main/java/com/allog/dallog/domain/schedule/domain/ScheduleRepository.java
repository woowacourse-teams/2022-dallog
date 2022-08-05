package com.allog.dallog.domain.schedule.domain;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT s "
            + "FROM Schedule s "
            + "WHERE s.period.startDateTime <= :endDate "
            + "AND s.period.endDateTime >= :startDate")
    List<Schedule> findByBetween(final LocalDateTime startDate,
                                 final LocalDateTime endDate);
}
