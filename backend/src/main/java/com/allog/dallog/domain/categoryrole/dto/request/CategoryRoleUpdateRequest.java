package com.allog.dallog.domain.categoryrole.dto.request;

import com.allog.dallog.domain.categoryrole.domain.CategoryRoleType;
import javax.validation.constraints.NotBlank;

public class CategoryRoleUpdateRequest {

    @NotBlank(message = "공백일 수 없습니다.")
    private CategoryRoleType categoryRoleType;

    private CategoryRoleUpdateRequest() {
    }

    public CategoryRoleUpdateRequest(final CategoryRoleType categoryRoleType) {
        this.categoryRoleType = categoryRoleType;
    }

    public CategoryRoleType getCategoryRoleType() {
        return categoryRoleType;
    }
}
