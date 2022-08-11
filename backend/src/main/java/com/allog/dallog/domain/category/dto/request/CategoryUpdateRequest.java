package com.allog.dallog.domain.category.dto.request;

import javax.validation.constraints.NotBlank;

public class CategoryUpdateRequest {

    @NotBlank(message = "공백일 수 없습니다.")
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
