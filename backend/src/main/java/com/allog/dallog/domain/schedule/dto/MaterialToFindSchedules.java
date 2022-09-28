package com.allog.dallog.domain.schedule.dto;

import com.allog.dallog.domain.category.domain.ExternalCategoryDetail;
import com.allog.dallog.domain.schedule.domain.IntegrationSchedule;
import com.allog.dallog.domain.subscription.domain.Subscriptions;
import java.util.ArrayList;
import java.util.List;

public class MaterialToFindSchedules {

    private final Subscriptions subscriptions;
    private final List<IntegrationSchedule> schedules;
    private final String refreshToken;
    private final List<ExternalCategoryDetail> externalCategoryDetails;

    public MaterialToFindSchedules(final Subscriptions subscriptions, final List<IntegrationSchedule> schedules,
                                   final String refreshToken,
                                   final List<ExternalCategoryDetail> externalCategoryDetails) {
        this.subscriptions = subscriptions;
        this.schedules = new ArrayList<>(schedules);
        this.refreshToken = refreshToken;
        this.externalCategoryDetails = new ArrayList<>(externalCategoryDetails);
    }

    public Subscriptions getSubscriptions() {
        return subscriptions;
    }

    public List<IntegrationSchedule> getSchedules() {
        return schedules;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public List<ExternalCategoryDetail> getExternalCategoryDetails() {
        return externalCategoryDetails;
    }
}
