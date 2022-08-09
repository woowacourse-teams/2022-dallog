package com.allog.dallog.domain.category.dto.request;

import javax.validation.constraints.NotBlank;

public class CategoryCreateRequest {

    @NotBlank
    private String name;

    private boolean isPrivate;

    private CategoryCreateRequest() {
    }

    public CategoryCreateRequest(final String name, final boolean isPrivate) {
        this.name = name;
        this.isPrivate = isPrivate;
    }

    public String getName() {
        return name;
    }

    public boolean getIsPrivate() {
        return isPrivate;
    }
}
