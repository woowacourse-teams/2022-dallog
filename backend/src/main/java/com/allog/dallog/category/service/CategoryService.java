package com.allog.dallog.category.service;

import com.allog.dallog.category.domain.Category;
import com.allog.dallog.category.domain.CategoryRepository;
import com.allog.dallog.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.category.dto.response.CategoryResponse;
import com.allog.dallog.category.exception.NoSuchCategoryException;
import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.service.MemberService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MemberService memberService;

    public CategoryService(final CategoryRepository categoryRepository, final MemberService memberService) {
        this.categoryRepository = categoryRepository;
        this.memberService = memberService;
    }

    @Transactional
    public CategoryResponse save(final Long memberId, final CategoryCreateRequest request) {
        Member member = memberService.getMember(memberId);
        Category category = categoryRepository.save(new Category(request.getName(), member));
        return new CategoryResponse(category);
    }

    public List<CategoryResponse> findAll(final Pageable pageable) {
        return categoryRepository.findSliceBy(pageable)
                .getContent()
                .stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }

    public CategoryResponse findById(final Long id) {
        return new CategoryResponse(getCategory(id));
    }

    public Category getCategory(final Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(NoSuchCategoryException::new);
    }
}
