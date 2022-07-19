package com.allog.dallog.category.dto.response;

import java.util.List;

public class CategoriesResponse {

    private int page;
    private List<CategoryResponse> data;

    public CategoriesResponse() {
    }

    public CategoriesResponse(final int page, final List<CategoryResponse> data) {
        this.page = page;
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public List<CategoryResponse> getData() {
        return data;
    }
}
