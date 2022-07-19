package com.allog.dallog.category.dto.response;

import java.util.List;

public class CategoriesResponse {

    private int page;
    private List<CategoryResponse> categories;

    public CategoriesResponse() {
    }

    public CategoriesResponse(final int page, final List<CategoryResponse> categories) {
        this.page = page;
        this.categories = categories;
    }

    public int getPage() {
        return page;
    }

    public List<CategoryResponse> getCategories() {
        return categories;
    }
}
