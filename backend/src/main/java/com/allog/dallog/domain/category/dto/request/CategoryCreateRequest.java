package com.allog.dallog.domain.category.dto.request;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryType;
import com.allog.dallog.domain.member.domain.Member;
import javax.validation.constraints.NotBlank;

public class CategoryCreateRequest {

    @NotBlank(message = "공백일 수 없습니다.")
    private String name;

    @NotBlank(message = "공백일 수 없습니다.")
    private String categoryType;

    private CategoryCreateRequest() {
    }

    public CategoryCreateRequest(final String name, final CategoryType categoryType) {
        this.name = name;
        this.categoryType = categoryType.name();
    }

    public Category toEntity(final Member member) {
        return new Category(name, member, CategoryType.from(categoryType));
    }

    public String getName() {
        return name;
    }

    public String getCategoryType() {
        return categoryType;
    }
}
