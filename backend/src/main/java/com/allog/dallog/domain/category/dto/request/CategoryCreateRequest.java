package com.allog.dallog.domain.category.dto.request;

import javax.validation.constraints.NotBlank;

public class CategoryCreateRequest {

    @NotBlank(message = "공백일 수 없습니다.")
    private String name;

    private boolean personal;

    private CategoryCreateRequest() {
    }

    public CategoryCreateRequest(final String name, final boolean personal) {
        this.name = name;
        this.personal = personal;
    }

    public String getName() {
        return name;
    }

    public boolean isPersonal() {
        return personal;
    }
}
