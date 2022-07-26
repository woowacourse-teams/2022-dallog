package com.allog.dallog.domain.category.dto.request;

import javax.validation.constraints.NotBlank;

public class CategoryCreateRequest {

    @NotBlank
    private String name;

    private CategoryCreateRequest() {
    }

    public CategoryCreateRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
