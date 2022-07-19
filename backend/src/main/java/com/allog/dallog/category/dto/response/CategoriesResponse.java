package com.allog.dallog.category.dto.response;

import java.util.List;

public class CategoriesResponse {

    private static final int START_PAGE_INDEX = 1;

    private int page;
    private List<CategoryResponse> data;

    public CategoriesResponse() {
    }

    public CategoriesResponse(final int page, final List<CategoryResponse> data) {
        this.page = page + START_PAGE_INDEX;
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public List<CategoryResponse> getData() {
        return data;
    }
}
