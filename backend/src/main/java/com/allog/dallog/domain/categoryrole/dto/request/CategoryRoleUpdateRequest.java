package com.allog.dallog.domain.categoryrole.dto.request;

import com.allog.dallog.domain.categoryrole.domain.CategoryRoleType;
import javax.validation.constraints.NotBlank;

public class CategoryRoleUpdateRequest {

    @NotBlank(message = "공백일 수 없습니다.")
    private Long memberId;

    @NotBlank(message = "공백일 수 없습니다.")
    private Long categoryId;


    @NotBlank(message = "공백일 수 없습니다.")
    private CategoryRoleType categoryRoleType;

    public CategoryRoleUpdateRequest() {
    }

    public CategoryRoleUpdateRequest(final Long memberId, final Long categoryId,
                                     final CategoryRoleType categoryRoleType) {
        this.memberId = memberId;
        this.categoryId = categoryId;
        this.categoryRoleType = categoryRoleType;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public CategoryRoleType getCategoryRoleType() {
        return categoryRoleType;
    }
}
