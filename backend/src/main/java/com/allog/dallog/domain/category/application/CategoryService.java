package com.allog.dallog.domain.category.application;

import static com.allog.dallog.domain.category.domain.CategoryType.NORMAL;
import static com.allog.dallog.domain.categoryrole.domain.CategoryAuthority.ADD_SCHEDULE;
import static com.allog.dallog.domain.categoryrole.domain.CategoryAuthority.UPDATE_SCHEDULE;
import static com.allog.dallog.domain.categoryrole.domain.CategoryRoleType.ADMIN;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.domain.CategoryRepository;
import com.allog.dallog.domain.category.domain.CategoryType;
import com.allog.dallog.domain.category.domain.ExternalCategoryDetail;
import com.allog.dallog.domain.category.domain.ExternalCategoryDetailRepository;
import com.allog.dallog.domain.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.domain.category.dto.request.CategoryUpdateRequest;
import com.allog.dallog.domain.category.dto.request.ExternalCategoryCreateRequest;
import com.allog.dallog.domain.category.dto.response.CategoriesResponse;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.category.exception.InvalidCategoryException;
import com.allog.dallog.domain.categoryrole.domain.CategoryAuthority;
import com.allog.dallog.domain.categoryrole.domain.CategoryRole;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleType;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.domain.MemberRepository;
import com.allog.dallog.domain.schedule.domain.ScheduleRepository;
import com.allog.dallog.domain.subscription.application.ColorPicker;
import com.allog.dallog.domain.subscription.domain.Color;
import com.allog.dallog.domain.subscription.domain.Subscription;
import com.allog.dallog.domain.subscription.domain.SubscriptionRepository;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ExternalCategoryDetailRepository externalCategoryDetailRepository;
    private final MemberRepository memberRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ScheduleRepository scheduleRepository;
    private final CategoryRoleRepository categoryRoleRepository;
    private final ColorPicker colorPicker;

    public CategoryService(final CategoryRepository categoryRepository,
                           final ExternalCategoryDetailRepository externalCategoryDetailRepository,
                           final MemberRepository memberRepository, final SubscriptionRepository subscriptionRepository,
                           final ScheduleRepository scheduleRepository,
                           final CategoryRoleRepository categoryRoleRepository, final ColorPicker colorPicker) {
        this.categoryRepository = categoryRepository;
        this.externalCategoryDetailRepository = externalCategoryDetailRepository;
        this.memberRepository = memberRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.scheduleRepository = scheduleRepository;
        this.categoryRoleRepository = categoryRoleRepository;
        this.colorPicker = colorPicker;
    }

    @Transactional
    public CategoryResponse save(final Long memberId, final CategoryCreateRequest request) {
        Member member = memberRepository.getById(memberId);
        Category category = request.toEntity(member);
        Category savedCategory = categoryRepository.save(category);

        subscribeCategory(member, category);
        createCategoryRoleAsAdminToCreator(member, category);
        return new CategoryResponse(savedCategory);
    }

    private void subscribeCategory(final Member member, final Category category) {
        Color color = Color.pick(colorPicker.pickNumber());
        subscriptionRepository.save(new Subscription(member, category, color));
    }

    private void createCategoryRoleAsAdminToCreator(final Member member, final Category category) {
        CategoryRole categoryRole = new CategoryRole(category, member, ADMIN);
        categoryRoleRepository.save(categoryRole);
    }

    @Transactional
    public CategoryResponse save(final Long memberId, final ExternalCategoryCreateRequest request) {
        List<Category> externalCategories = categoryRepository
                .findByMemberIdAndCategoryType(memberId, CategoryType.GOOGLE);
        externalCategoryDetailRepository
                .validateExistByExternalIdAndCategoryIn(request.getExternalId(), externalCategories);

        CategoryResponse response = save(memberId, new CategoryCreateRequest(request.getName(), CategoryType.GOOGLE));
        Category category = categoryRepository.getById(response.getId());
        externalCategoryDetailRepository.save(new ExternalCategoryDetail(category, request.getExternalId()));

        return response;
    }

    public CategoriesResponse findNormalByName(final String name, final Pageable pageable) {
        List<Category> categories
                = categoryRepository.findByNameContainingAndCategoryType(name, NORMAL, pageable).getContent();

        return new CategoriesResponse(pageable.getPageNumber(), categories);
    }

    public CategoriesResponse findMyCategories(final Long memberId, final String name, final Pageable pageable) {
        List<Category> categories
                = categoryRepository.findByMemberIdAndNameContaining(memberId, name, pageable).getContent();

        return new CategoriesResponse(pageable.getPageNumber(), categories);
    }

    // 멤버가 ADMIN이 아니어도 일정 추가/제거/수정이 가능하므로, findAdminCategories와 별도의 메소드로 분리해야함
    public CategoriesResponse findScheduleEditableCategories(final Long memberId, final Pageable pageable) {
        Set<CategoryRoleType> roleTypes = CategoryRoleType.getHavingAuthorities(Set.of(ADD_SCHEDULE, UPDATE_SCHEDULE));
        List<Category> categories = categoryRepository.findByMemberIdAndCategoryRoleTypes(memberId, roleTypes,
                pageable);

        return new CategoriesResponse(pageable.getPageNumber(), categories);
    }

    public CategoriesResponse findAdminCategories(final Long memberId, final Pageable pageable) {
        List<Category> categories = categoryRepository.findByMemberIdAndCategoryRoleTypes(memberId, Set.of(ADMIN),
                pageable);
        return new CategoriesResponse(pageable.getPageNumber(), categories);
    }

    public CategoryResponse findById(final Long id) {
        Category category = categoryRepository.getById(id);
        return new CategoryResponse(category);
    }

    @Transactional
    public void update(final Long memberId, final Long id, final CategoryUpdateRequest request) {
        Category category = categoryRepository.getById(id);

        CategoryRole role = categoryRoleRepository.getByMemberIdAndCategoryId(memberId, category.getId());
        role.validateAuthority(CategoryAuthority.UPDATE_CATEGORY);

        category.changeName(request.getName());
    }

    @Transactional
    public void delete(final Long memberId, final Long id) {
        Category category = categoryRepository.getById(id);

        validateNotPersonalCategory(category);

        CategoryRole role = categoryRoleRepository.getByMemberIdAndCategoryId(memberId, category.getId());
        role.validateAuthority(CategoryAuthority.DELETE_CATEGORY);

        scheduleRepository.deleteByCategoryIdIn(List.of(id));
        subscriptionRepository.deleteByCategoryIdIn(List.of(id));
        externalCategoryDetailRepository.deleteByCategoryId(id);
        categoryRoleRepository.deleteByCategoryId(id);
        categoryRepository.deleteById(id);
    }

    private void validateNotPersonalCategory(final Category category) {
        if (category.isPersonal()) {
            throw new InvalidCategoryException("내 일정 카테고리는 삭제할 수 없습니다.");
        }
    }
}
