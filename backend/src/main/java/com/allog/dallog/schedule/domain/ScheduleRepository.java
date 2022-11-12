package com.allog.dallog.schedule.domain;

import com.allog.dallog.category.domain.Category;
import com.allog.dallog.schedule.exception.NoSuchScheduleException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    void deleteByCategoryIdIn(final List<Long> categoryIds);

    @Query("SELECT s "
            + "FROM Schedule s "
            + "JOIN s.category c "
            + "WHERE c IN :categories "
            + "AND s.startDateTime <= :endDate "
            + "AND s.endDateTime >= :startDate")
    List<Schedule> findByCategoriesAndBetween(final List<Category> categories, final LocalDateTime startDate,
                                              final LocalDateTime endDate);

    default Schedule getById(final Long id) {
        return this.findById(id)
                .orElseThrow(NoSuchScheduleException::new);
    }

    default List<IntegrationSchedule> getByCategoriesAndBetween(final List<Category> categories,
                                                                final LocalDateTime startDateTime,
                                                                final LocalDateTime endDateTime) {
        if (categories.isEmpty()) {
            return new ArrayList<>();
        }

        List<Schedule> schedules = findByCategoriesAndBetween(categories, startDateTime, endDateTime);
        return schedules.stream()
                .map(IntegrationSchedule::new)
                .collect(Collectors.toList());
    }
}
