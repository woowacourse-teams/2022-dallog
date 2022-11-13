package com.allog.dallog.category.dto.request;

import javax.validation.constraints.NotBlank;

public class CategoryUpdateRequest {

    @NotBlank(message = "카테고리 이름이 공백일 수 없습니다.")
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
