package com.allog.dallog.domain.categoryrole.application;

import static com.allog.dallog.domain.categoryrole.domain.CategoryRoleType.ADMIN;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.categoryrole.domain.CategoryAuthority;
import com.allog.dallog.domain.categoryrole.domain.CategoryRole;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleType;
import com.allog.dallog.domain.categoryrole.dto.request.CategoryRoleUpdateRequest;
import com.allog.dallog.domain.categoryrole.exception.ManagingCategoryLimitExcessException;
import com.allog.dallog.domain.categoryrole.exception.NotAbleToChangeRoleException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class CategoryRoleService {

    private static final int MAX_MANAGING_CATEGORY_COUNT = 50;

    private final CategoryRoleRepository categoryRoleRepository;

    public CategoryRoleService(final CategoryRoleRepository categoryRoleRepository) {
        this.categoryRoleRepository = categoryRoleRepository;
    }

    @Transactional
    public void updateRole(final Long loginMemberId, final Long memberId, final Long categoryId,
                           final CategoryRoleUpdateRequest request) {
        CategoryRoleType roleType = request.getCategoryRoleType();

        validateAuthority(loginMemberId, categoryId);
        validateSoleAdmin(memberId, categoryId);
        validateManagingCategoryLimit(memberId, roleType);

        CategoryRole categoryRole = categoryRoleRepository.getByMemberIdAndCategoryId(memberId, categoryId);

        validateCategoryType(categoryRole);

        categoryRole.changeRole(roleType);
    }

    private void validateAuthority(final Long loginMemberId, final Long categoryId) {
        CategoryRole loginMemberCategoryRole = categoryRoleRepository.getByMemberIdAndCategoryId(loginMemberId,
                categoryId);
        loginMemberCategoryRole.validateAuthority(CategoryAuthority.CHANGE_ROLE_OF_SUBSCRIBER);
    }

    private void validateSoleAdmin(final Long memberId, final Long categoryId) {
        boolean isSoleAdmin = categoryRoleRepository.isMemberSoleAdminInCategory(memberId, categoryId);
        if (isSoleAdmin) {
            throw new NotAbleToChangeRoleException("변경 대상 회원이 유일한 ADMIN이므로 다른 역할로 변경할 수 없습니다.");
        }
    }

    private void validateManagingCategoryLimit(final Long memberId, final CategoryRoleType roleType) {
        long memberAdminCount = categoryRoleRepository.findByMemberId(memberId)
                .stream()
                .filter(CategoryRole::isAdmin)
                .count();

        if (roleType.equals(ADMIN) && memberAdminCount >= MAX_MANAGING_CATEGORY_COUNT) {
            throw new ManagingCategoryLimitExcessException();
        }
    }

    private void validateCategoryType(final CategoryRole categoryRole) {
        Category category = categoryRole.getCategory();

        if (!category.isNormal()) {
            throw new NotAbleToChangeRoleException("개인 카테고리 또는 외부 카테고리에 대한 회원의 역할을 변경할 수 없습니다.");
        }
    }
}
