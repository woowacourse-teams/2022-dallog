package com.allog.dallog.domain.category.dto.request;

import com.allog.dallog.domain.category.domain.CategoryType;
import javax.validation.constraints.NotBlank;

public class CategoryCreateRequest {

    @NotBlank(message = "공백일 수 없습니다.")
    private String name;

    private String categoryType;

    private CategoryCreateRequest() {
    }

    public CategoryCreateRequest(final String name, final CategoryType categoryType) {
        this.name = name;
        this.categoryType = categoryType.name();
    }

    public String getName() {
        return name;
    }

    public String getCategoryType() {
        return categoryType;
    }
}
