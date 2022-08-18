package com.allog.dallog.domain.category.dto.response;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.member.dto.MemberResponse;
import java.time.LocalDateTime;

public class CategoryResponse {

    private Long id;
    private String name;
    private String categoryType;
    private MemberResponse creator;
    private LocalDateTime createdAt;

    private CategoryResponse() {
    }

    public CategoryResponse(final Category category) {
        this(category.getId(), category.getName(), category.getCategoryType().name(),
                new MemberResponse(category.getMember()), category.getCreatedAt());
    }

    public CategoryResponse(final Long id, final String name, final String categoryType, final MemberResponse creator,
                            final LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.categoryType = categoryType;
        this.creator = creator;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public MemberResponse getCreator() {
        return creator;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
