package com.allog.dallog.category.service;

import com.allog.dallog.category.domain.Category;
import com.allog.dallog.category.domain.CategoryRepository;
import com.allog.dallog.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.category.dto.response.CategoryResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Long save(final CategoryCreateRequest request) {
        Category category = categoryRepository.save(request.toEntity());
        return category.getId();
    }

    public List<CategoryResponse> findSliceBy(final PageRequest request) {
        return categoryRepository.findSliceBy(request)
                .getContent()
                .stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }
}
