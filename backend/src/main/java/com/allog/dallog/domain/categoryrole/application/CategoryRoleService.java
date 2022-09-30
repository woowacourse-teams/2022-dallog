package com.allog.dallog.domain.categoryrole.application;

import com.allog.dallog.domain.categoryrole.domain.CategoryAuthority;
import com.allog.dallog.domain.categoryrole.domain.CategoryRole;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleRepository;
import com.allog.dallog.domain.categoryrole.domain.CategoryRoleType;
import com.allog.dallog.domain.categoryrole.dto.request.CategoryRoleUpdateRequest;
import com.allog.dallog.domain.categoryrole.exception.NotAbleToMangeRoleException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class CategoryRoleService {

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

        CategoryRole categoryRole = categoryRoleRepository.getByMemberIdAndCategoryId(memberId, categoryId);
        categoryRole.changeRole(roleType);
    }

    private void validateAuthority(final Long loginMemberId, final Long categoryId) {
        CategoryRole loginMemberCategoryRole = categoryRoleRepository.getByMemberIdAndCategoryId(loginMemberId,
                categoryId);
        loginMemberCategoryRole.validateAuthority(CategoryAuthority.MANAGE_ROLE);
    }

    private void validateSoleAdmin(final Long loginMemberId, final Long categoryId) {
        boolean isSoleAdmin = categoryRoleRepository.isMemberSoleAdminInCategory(loginMemberId, categoryId);
        if (isSoleAdmin) {
            throw new NotAbleToMangeRoleException("변경 대상 회원이 유일한 ADMIN이므로 다른 역할로 변경할 수 없습니다.");
        }
    }
}
