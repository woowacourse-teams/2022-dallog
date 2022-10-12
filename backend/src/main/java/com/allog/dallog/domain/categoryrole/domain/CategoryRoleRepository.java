package com.allog.dallog.domain.categoryrole.domain;

import com.allog.dallog.domain.categoryrole.exception.NoSuchCategoryRoleException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRoleRepository extends JpaRepository<CategoryRole, Long> {

    @Query("SELECT cr "
            + "FROM CategoryRole cr "
            + "WHERE cr.member.id = :memberId AND cr.category.id = :categoryId")
    Optional<CategoryRole> findByMemberIdAndCategoryId(final Long memberId, final Long categoryId);

    @EntityGraph(attributePaths = {"member"})
    List<CategoryRole> findByCategoryId(final Long categoryId);

    @EntityGraph(attributePaths = {"category", "category.member"})
    List<CategoryRole> findByMemberId(final Long memberId);

    int countByCategoryIdAndCategoryRoleType(final Long categoryId, final CategoryRoleType categoryRoleType);

    void deleteByCategoryId(final Long categoryId);

    void deleteByMemberId(final Long memberId);

    void deleteByCategoryIdIn(final List<Long> categoryIds);

    void deleteByMemberIdAndCategoryId(final Long memberId, final Long categoryId);

    default CategoryRole getByMemberIdAndCategoryId(final Long memberId, final Long categoryId) {
        return findByMemberIdAndCategoryId(memberId, categoryId)
                .orElseThrow(NoSuchCategoryRoleException::new);
    }

    default boolean isMemberSoleAdminInCategory(final Long memberId, final Long categoryId) {
        CategoryRole categoryRole = getByMemberIdAndCategoryId(memberId, categoryId);
        int adminCount = countByCategoryIdAndCategoryRoleType(categoryId, CategoryRoleType.ADMIN);

        return categoryRole.isAdmin() && adminCount == 1;
    }

    default CategoryRole getByCategoryId(final Long categoryId) {
        List<CategoryRole> categoryRoles = findByCategoryId(categoryId);
        return categoryRoles.get(0);
    }
}
