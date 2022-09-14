package com.allog.dallog.domain.schedule.dto.response;

import com.allog.dallog.domain.integrationschedule.domain.IntegrationSchedule;
import com.allog.dallog.domain.subscription.domain.Color;
import java.time.LocalDateTime;

public class MemberScheduleResponse {

    private final String id;
    private final String title;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final String memo;
    private final Long categoryId;
    private final String colorCode;
    private final String categoryType;

    public MemberScheduleResponse(final IntegrationSchedule integrationSchedule, final Color color) {
        this(integrationSchedule.getId(), integrationSchedule.getTitle(), integrationSchedule.getStartDateTime(),
                integrationSchedule.getEndDateTime(), integrationSchedule.getMemo(),
                integrationSchedule.getCategoryId(), color.getColorCode(),
                integrationSchedule.getCategoryType().name());
    }

    public MemberScheduleResponse(final String id, final String title, final LocalDateTime startDateTime,
                                  final LocalDateTime endDateTime, final String memo, final Long categoryId,
                                  final String colorCode, final String categoryType) {
        this.id = id;
        this.title = title;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.memo = memo;
        this.categoryId = categoryId;
        this.colorCode = colorCode;
        this.categoryType = categoryType;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public String getMemo() {
        return memo;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getColorCode() {
        return colorCode;
    }

    public String getCategoryType() {
        return categoryType;
    }
}
