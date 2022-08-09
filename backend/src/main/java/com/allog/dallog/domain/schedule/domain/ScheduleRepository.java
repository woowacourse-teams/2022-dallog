package com.allog.dallog.domain.schedule.domain;

import com.allog.dallog.domain.category.domain.Category;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s "
            + "FROM Schedule s "
            + "JOIN s.category c "
            + "WHERE c = :category "
            + "AND s.period.startDateTime <= :endDate "
            + "AND s.period.endDateTime >= :startDate")
    List<Schedule> findByCategoryIdAndBetween(final Category category, final LocalDateTime startDate,
                                              final LocalDateTime endDate);
}
