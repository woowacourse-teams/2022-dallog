package com.allog.dallog.domain.composition.application;

import com.allog.dallog.domain.category.application.CategoryService;
import com.allog.dallog.domain.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.domain.category.dto.request.ExternalCategoryCreateRequest;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.subscription.application.SubscriptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class CategorySubscriptionService {

    private final CategoryService categoryService;
    private final SubscriptionService subscriptionService;

    public CategorySubscriptionService(final CategoryService categoryService,
                                       final SubscriptionService subscriptionService) {
        this.categoryService = categoryService;
        this.subscriptionService = subscriptionService;
    }

    @Transactional
    public CategoryResponse save(final Long memberId, final CategoryCreateRequest request) {
        CategoryResponse response = categoryService.saveCategory(memberId, request);
        subscriptionService.save(memberId, response.getId());
        return response;
    }

    @Transactional
    public CategoryResponse save(final Long memberId, final ExternalCategoryCreateRequest request) {
        CategoryResponse response = categoryService.saveExternalCategory(memberId, request);
        subscriptionService.save(memberId, response.getId());
        return response;
    }
}
