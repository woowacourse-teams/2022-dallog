package com.allog.dallog.category.dto.response;

import com.allog.dallog.category.domain.Category;
import com.allog.dallog.member.dto.response.MemberResponse;
import java.time.LocalDateTime;

public class CategoryDetailResponse {

    private Long id;
    private String name;
    private String categoryType;
    private int subscriberCount;
    private MemberResponse creator;
    private LocalDateTime createdAt;

    private CategoryDetailResponse() {
    }

    public CategoryDetailResponse(final Category category, final int subscriberCount) {
        this(category.getId(), category.getName(), category.getCategoryType().name(), subscriberCount,
                new MemberResponse(category.getMember()), category.getCreatedAt());
    }

    public CategoryDetailResponse(final Long id, final String name, final String categoryType,
                                  final int subscriberCount, final MemberResponse creator,
                                  final LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.categoryType = categoryType;
        this.subscriberCount = subscriberCount;
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

    public int getSubscriberCount() {
        return subscriberCount;
    }

    public MemberResponse getCreator() {
        return creator;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
