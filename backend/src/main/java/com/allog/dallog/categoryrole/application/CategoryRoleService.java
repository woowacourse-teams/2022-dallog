package com.allog.dallog.categoryrole.application;

import com.allog.dallog.categoryrole.domain.CategoryAuthority;
import com.allog.dallog.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.categoryrole.dto.request.CategoryRoleUpdateRequest;
import com.allog.dallog.categoryrole.dto.response.SubscribersResponse;
import com.allog.dallog.category.domain.Category;
import com.allog.dallog.categoryrole.domain.CategoryRole;
import com.allog.dallog.categoryrole.exception.NotAbleToChangeRoleException;
import java.util.List;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class CategoryRoleService {

    private final CategoryRoleRepository categoryRoleRepository;

    public CategoryRoleService(final CategoryRoleRepository categoryRoleRepository) {
        this.categoryRoleRepository = categoryRoleRepository;
    }

    public SubscribersResponse findSubscribers(final Long loginMemberId, final Long categoryId) {
        CategoryRole categoryRole = categoryRoleRepository.getByMemberIdAndCategoryId(loginMemberId, categoryId);
        categoryRole.validateAuthority(CategoryAuthority.FIND_SUBSCRIBERS);

        List<CategoryRole> categoryRoles = categoryRoleRepository.findByCategoryId(categoryId);
        return new SubscribersResponse(categoryRoles);
    }

    @Transactional
    public void updateRole(final Long loginMemberId, final Long memberId, final Long categoryId,
                           final CategoryRoleUpdateRequest request) {
        try {
            List<CategoryRole> categoryRolesInCategory = categoryRoleRepository.findByCategoryId(categoryId);
            CategoryRole roleOfTargetMember = getCategoryRole(memberId, categoryRolesInCategory);

            validateLoginMemberAuthority(loginMemberId, categoryRolesInCategory); // 요청 유저 권한 검증
            validateIsTargetMemberSoleAdmin(categoryRolesInCategory, roleOfTargetMember); // 대상 유저가 유일한 어드민이 아닌지 검증
            validateCategoryType(roleOfTargetMember.getCategory()); // 카테고리가 개인, 외부 카테고리가 아닌지 검증
            categoryRoleRepository.validateManagingCategoryLimit(memberId, request.getCategoryRoleType()); // 관리 개수 검증

            roleOfTargetMember.changeRole(request.getCategoryRoleType());
        } catch (final ObjectOptimisticLockingFailureException e) {
            throw NotAbleToChangeRoleException.concurrentIssue();
        }
    }

    private CategoryRole getCategoryRole(final Long memberId, final List<CategoryRole> categoryRoles) {
        return categoryRoles.stream()
                .filter(it -> it.getMember().getId().equals(memberId))
                .findFirst()
                .orElseThrow();
    }

    private void validateLoginMemberAuthority(final Long loginMemberId, final List<CategoryRole> categoryRoles) {
        CategoryRole loginMemberCategoryRole = categoryRoles.stream()
                .filter(categoryRole -> categoryRole.getMember().getId().equals(loginMemberId))
                .findFirst()
                .orElseThrow();

        loginMemberCategoryRole.validateAuthority(CategoryAuthority.CHANGE_ROLE_OF_SUBSCRIBER);
    }

    private void validateIsTargetMemberSoleAdmin(final List<CategoryRole> categoryRoles,
                                                 final CategoryRole categoryRole) {
        if (categoryRole.isAdmin() && categoryRoles.size() == 1) {
            throw new NotAbleToChangeRoleException();
        }
    }

    private void validateCategoryType(final Category category) {
        if (!category.isNormal()) {
            throw new NotAbleToChangeRoleException("개인 카테고리 또는 외부 카테고리에 대한 회원의 역할을 변경할 수 없습니다.");
        }
    }
}
