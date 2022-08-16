package com.allog.dallog.domain.category.dto.request;

public class ExternalCategoryCreateRequest {

    private String externalId;
    private String name;

    private ExternalCategoryCreateRequest() {
    }

    public ExternalCategoryCreateRequest(final String externalId, final String name) {
        this.externalId = externalId;
        this.name = name;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getName() {
        return name;
    }
}
