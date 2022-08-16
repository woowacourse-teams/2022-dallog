package com.allog.dallog.domain.category.application;

import com.allog.dallog.domain.auth.exception.NoPermissionException;
import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.domain.category.dto.request.CategoryUpdateRequest;
import com.allog.dallog.domain.category.dto.response.CategoriesResponse;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.category.exception.NoSuchCategoryException;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.member.exception.NoSuchMemberException;
import com.allog.dallog.domain.schedule.domain.ScheduleRepository;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ScheduleRepository scheduleRepository;

    public CategoryService(final CategoryRepository categoryRepository, final MemberRepository memberRepository,
                           final SubscriptionRepository subscriptionRepository,
                           final ScheduleRepository scheduleRepository) {
        this.categoryRepository = categoryRepository;
        this.memberRepository = memberRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional
    public CategoryResponse save(final Long memberId, final CategoryCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NoSuchMemberException::new);
        Category newCategory = new Category(request.getName(), member, request.isPersonal());
        categoryRepository.save(newCategory);
        return new CategoryResponse(newCategory);
    }

    public CategoriesResponse findAllByName(final String name, final Pageable pageable) {
        List<Category> categories = categoryRepository.findAllLikeCategoryName(name, pageable).getContent();

        return new CategoriesResponse(pageable.getPageNumber(), categories);
    }

    public CategoriesResponse findMineByName(final Long memberId, final String name, final Pageable pageable) {
        List<Category> categories = categoryRepository.findByMemberIdLikeCategoryName(memberId, name, pageable).getContent();

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
    public void deleteById(final Long memberId, final Long categoryId) {
        validateCategoryExisting(categoryId);
        validatePermission(memberId, categoryId);

        scheduleRepository.deleteByCategoryIdIn(List.of(categoryId));
        subscriptionRepository.deleteByCategoryIdIn(List.of(categoryId));
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

    @Transactional
    public void deleteByMemberId(final Long memberId) {
        List<Long> categoryIds = categoryRepository.findByMemberId(memberId)
                .stream()
                .map(Category::getId)
                .collect(Collectors.toList());

        scheduleRepository.deleteByCategoryIdIn(categoryIds);
        subscriptionRepository.deleteByCategoryIdIn(categoryIds);
        categoryRepository.deleteByMemberId(memberId);
    }

    public void validateCreatorBy(final Long memberId, final Category category) {
        if (!category.isCreator(memberId)) {
            throw new NoPermissionException();
        }
    }
}
