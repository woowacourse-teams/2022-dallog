package com.allog.dallog.domain.category.dto.response;

import com.allog.dallog.domain.category.domain.Category;
import java.util.List;
import java.util.stream.Collectors;

public class CategoriesNoPageResponse {

    private List<CategoryResponse> categories;

    private CategoriesNoPageResponse() {
    }

    public CategoriesNoPageResponse(final List<Category> categories) {
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
