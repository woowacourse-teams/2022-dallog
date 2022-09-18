package com.allog.dallog.domain.schedule.domain;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.integrationschedule.domain.IntegrationSchedule;
import com.allog.dallog.domain.schedule.exception.NoSuchScheduleException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    void deleteByCategoryIdIn(final List<Long> categoryIds);

    @Query("SELECT s "
            + "FROM Schedule s "
            + "JOIN s.category c "
            + "WHERE c = :category "
            + "AND s.startDateTime <= :endDate "
            + "AND s.endDateTime >= :startDate")
    List<Schedule> findByCategoryAndBetween(final Category category, final LocalDateTime startDate,
                                            final LocalDateTime endDate);

    default Schedule getById(final Long id) {
        return this.findById(id)
                .orElseThrow(NoSuchScheduleException::new);
    }

    default List<IntegrationSchedule> createByCategoryAndBetween(
            final Category category, final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        List<Schedule> schedules = findByCategoryAndBetween(category, startDateTime, endDateTime);

        return schedules.stream()
                .map(schedule -> new IntegrationSchedule(schedule, category.getCategoryType()))
                .collect(Collectors.toList());
    }
}
