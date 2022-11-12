package com.allog.dallog.category.dto.response;

import com.allog.dallog.category.domain.Category;
import java.util.List;
import java.util.stream.Collectors;

public class CategoriesResponse {

    private List<CategoryResponse> categories;

    private CategoriesResponse() {
    }

    public CategoriesResponse(final List<Category> categories) {
        this.categories = toResponses(categories);
    }

    private List<CategoryResponse> toResponses(final List<Category> categories) {
        return categories.stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> getCategories() {
        return categories;
    }
}
