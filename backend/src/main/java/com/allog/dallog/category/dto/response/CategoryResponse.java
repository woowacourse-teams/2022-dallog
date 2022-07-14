package com.allog.dallog.category.dto.response;

import com.allog.dallog.category.domain.Category;
import java.time.LocalDateTime;

public class CategoryResponse {

    private final Long id;
    private final String name;
    private final LocalDateTime createdAt;

    public CategoryResponse(final Category category) {
        this(category.getId(), category.getName(), category.getCreatedAt());
    }

    public CategoryResponse(final Long id, final String name, final LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
