package com.allog.dallog.domain.category.dto.request;

import javax.validation.constraints.NotBlank;

public class ExternalCategoryCreateRequest {

    @NotBlank(message = "공백일 수 없습니다.")
    private String externalId;

    @NotBlank(message = "공백일 수 없습니다.")
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
