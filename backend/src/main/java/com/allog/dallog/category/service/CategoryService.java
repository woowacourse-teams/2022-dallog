package com.allog.dallog.category.service;

import com.allog.dallog.auth.exception.NoPermissionException;
import com.allog.dallog.category.domain.Category;
import com.allog.dallog.category.domain.CategoryRepository;
import com.allog.dallog.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.category.dto.request.CategoryUpdateRequest;
import com.allog.dallog.category.dto.response.CategoriesResponse;
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

    public CategoriesResponse findAll(final Pageable pageable) {
        List<CategoryResponse> categoryResponses = categoryRepository.findSliceBy(pageable)
                .getContent()
                .stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
        return new CategoriesResponse(pageable.getPageNumber(), categoryResponses);
    }

    public CategoriesResponse findMine(final Long memberId, final Pageable pageable) {
        Member member = memberService.getMember(memberId);

        List<CategoryResponse> categoryResponses = categoryRepository.findSliceByMemberId(pageable, member.getId())
                .getContent()
                .stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
        return new CategoriesResponse(pageable.getPageNumber(), categoryResponses);
    }

    public CategoryResponse findById(final Long id) {
        return new CategoryResponse(getCategory(id));
    }

    public Category getCategory(final Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(NoSuchCategoryException::new);
    }

    @Transactional
    public void update(final Long memberId, final Long categoryId, final CategoryUpdateRequest request) {
        memberService.getMember(memberId);
        Category category = getCategory(categoryId);

        if (!categoryRepository.existsByIdAndMemberId(category.getId(), memberId)) {
            throw new NoPermissionException();
        }

        category.changeName(request.getName());
    }
}
