package com.allog.dallog.domain.category.dto.response;

import com.allog.dallog.domain.category.domain.Category;
import java.util.List;
import java.util.stream.Collectors;

public class CategoriesResponse {

    private int page;
    private List<CategoryResponse> categories;

    public CategoriesResponse() {
    }

    public CategoriesResponse(final int page, final List<Category> categories) {
        this.page = page;
        this.categories = toDtos(categories);
    }

    private List<CategoryResponse> toDtos(final List<Category> categories) {
        return categories.stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }

    public int getPage() {
        return page;
    }

    public List<CategoryResponse> getCategories() {
        return categories;
    }
}
