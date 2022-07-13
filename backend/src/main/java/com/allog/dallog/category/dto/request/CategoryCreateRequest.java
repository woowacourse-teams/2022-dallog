package com.allog.dallog.category.dto.request;

import com.allog.dallog.category.domain.Category;
import javax.validation.constraints.NotBlank;

public class CategoryCreateRequest {

    @NotBlank
    private String name;

    private CategoryCreateRequest() {
    }

    public CategoryCreateRequest(final String name) {
        this.name = name;
    }

    public Category toEntity() {
        return new Category(name);
    }

    public String getName() {
        return name;
    }
}
