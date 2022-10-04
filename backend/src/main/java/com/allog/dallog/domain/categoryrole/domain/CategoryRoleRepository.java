package com.allog.dallog.domain.categoryrole.domain;

import com.allog.dallog.domain.categoryrole.exception.NoSuchCategoryRoleException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRoleRepository extends JpaRepository<CategoryRole, Long> {

    Optional<CategoryRole> findByMemberIdAndCategoryId(final Long memberId, final Long categoryId);

    List<CategoryRole> findByCategoryId(final Long categoryId);

    int countByCategoryIdAndCategoryRoleType(final Long categoryId, final CategoryRoleType categoryRoleType);

    void deleteByCategoryId(final Long categoryId);

    void deleteByMemberId(final Long memberId);

    default CategoryRole getByMemberIdAndCategoryId(final Long memberId, final Long categoryId) {
        return findByMemberIdAndCategoryId(memberId, categoryId)
                .orElseThrow(NoSuchCategoryRoleException::new);
    }

    default boolean isMemberSoleAdminInCategory(final Long memberId, final Long categoryId) {
        CategoryRole categoryRole = getByMemberIdAndCategoryId(memberId, categoryId);
        int adminCount = countByCategoryIdAndCategoryRoleType(categoryId, CategoryRoleType.ADMIN);

        return categoryRole.isAdmin() && adminCount == 1;
    }
}
