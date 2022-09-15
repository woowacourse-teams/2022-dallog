package com.allog.dallog.domain.integrationschedule.dao;

import com.allog.dallog.domain.category.domain.CategoryType;
import com.allog.dallog.domain.integrationschedule.domain.IntegrationSchedule;
import com.allog.dallog.domain.integrationschedule.domain.Period;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class IntegrationScheduleDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public IntegrationScheduleDao(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<IntegrationSchedule> findByCategoryIdInAndBetween(final List<Long> categoryIds,
                                                                  final LocalDateTime startDateTime,
                                                                  final LocalDateTime endDateTime) {
        if (categoryIds.isEmpty()) {
            return new ArrayList<>();
        }

        String sql = "SELECT s.id as id, c.id as categoryId, s.title as title, "
                + "s.start_date_time as startDateTime, s.end_date_time as endDateTime, "
                + "s.memo as memo, c.category_type as categoryType "
                + "FROM schedules s "
                + "JOIN categories c ON s.categories_id = c.id "
                + "WHERE c.id IN (:categoryIds) "
                + "AND s.start_date_time <= :endDateTime "
                + "AND s.end_date_time >= :startDateTime ";

        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("categoryIds", categoryIds)
                .addValue("startDateTime", startDateTime.toString())
                .addValue("endDateTime", endDateTime.toString());

        return jdbcTemplate.query(sql, parameterSource, generateIntegrationSchedule());
    }

    private RowMapper<IntegrationSchedule> generateIntegrationSchedule() {
        return (resultSet, rowNum) -> {
            Long id = resultSet.getLong("id");
            Long categoryId = resultSet.getLong("categoryId");
            String title = resultSet.getString("title");
            LocalDateTime startDateTime = resultSet.getTimestamp("startDateTime").toLocalDateTime();
            LocalDateTime endDateTime = resultSet.getTimestamp("endDateTime").toLocalDateTime();
            String memo = resultSet.getString("memo");
            CategoryType categoryType = CategoryType.from((resultSet.getString("categoryType")));

            Period period = new Period(startDateTime, endDateTime);

            return new IntegrationSchedule(String.valueOf(id), categoryId, title, period, memo, categoryType);
        };
    }
}
