package com.allog.dallog.category.dto.request;

import javax.validation.constraints.NotBlank;

public class CategoryUpdateRequest {

    @NotBlank
    private String name;

    private CategoryUpdateRequest() {
    }

    public CategoryUpdateRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
