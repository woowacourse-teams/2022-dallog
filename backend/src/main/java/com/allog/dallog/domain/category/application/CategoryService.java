package com.allog.dallog.domain.category.application;

import com.allog.dallog.domain.auth.exception.NoPermissionException;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.domain.category.dto.request.CategoryUpdateRequest;
import com.allog.dallog.domain.category.dto.response.CategoriesResponse;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.category.exception.NoSuchCategoryException;
import com.allog.dallog.domain.member.application.MemberService;
import com.allog.dallog.domain.member.domain.Member;
import java.util.List;
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

    public CategoriesResponse findAllByName(final String name, final Pageable pageable) {
        List<Category> categories = categoryRepository.findAllLikeCategoryName(name, pageable).getContent();

        return new CategoriesResponse(pageable.getPageNumber(), categories);
    }

    public CategoriesResponse findMine(final Long memberId, final Pageable pageable) {
        List<Category> categories = categoryRepository.findSliceByMemberId(memberId, pageable).getContent();

        return new CategoriesResponse(pageable.getPageNumber(), categories);
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
        Category category = getCategory(categoryId);

        validatePermission(memberId, categoryId);

        category.changeName(request.getName());
    }

    @Transactional
    public void delete(final Long memberId, final Long categoryId) {
        validateCategoryExisting(categoryId);
        validatePermission(memberId, categoryId);

        categoryRepository.deleteById(categoryId);
    }

    private void validateCategoryExisting(final Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new NoSuchCategoryException("존재하지 않는 카테고리를 삭제할 수 없습니다.");
        }
    }

    private void validatePermission(final Long memberId, final Long categoryId) {
        if (!categoryRepository.existsByIdAndMemberId(categoryId, memberId)) {
            throw new NoPermissionException();
        }
    }

    public void validateCreatorBy(final Long memberId, final Category category) {
        if (!category.isCreator(memberId)) {
            throw new NoPermissionException();
        }
    }
}
