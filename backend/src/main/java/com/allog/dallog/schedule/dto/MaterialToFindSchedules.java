package com.allog.dallog.schedule.dto;

import com.allog.dallog.category.domain.ExternalCategoryDetail;
import com.allog.dallog.schedule.domain.IntegrationSchedule;
import com.allog.dallog.subscription.domain.Subscriptions;
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
