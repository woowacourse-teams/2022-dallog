package com.allog.dallog.domain.schedule.dto;

import com.allog.dallog.domain.schedule.domain.IntegrationSchedule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvailablePeriodMaterial {

    private final List<IntegrationSchedule> internalSchedules;
    private final Map<String, List<ExternalRequestMaterial>> tokenByExternalIds;

    public AvailablePeriodMaterial(final List<IntegrationSchedule> internalSchedules,
                                   final Map<String, List<ExternalRequestMaterial>> tokenByExternalIds) {
        this.internalSchedules = new ArrayList<>(internalSchedules);
        this.tokenByExternalIds = new HashMap<>(tokenByExternalIds);
    }

    public List<IntegrationSchedule> getInternalSchedules() {
        return new ArrayList<>(internalSchedules);
    }

    public Map<String, List<ExternalRequestMaterial>> getTokenByExternalIds() {
        return new HashMap<>(tokenByExternalIds);
    }
}
