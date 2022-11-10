package com.allog.dallog.domain.categoryrole.domain;

import static com.allog.dallog.domain.categoryrole.domain.CategoryRoleType.ADMIN;
import static com.allog.dallog.domain.categoryrole.domain.CategoryRoleType.NONE;

import com.allog.dallog.domain.categoryrole.exception.ManagingCategoryLimitExcessException;
import com.allog.dallog.domain.categoryrole.exception.NoSuchCategoryRoleException;
import java.util.List;
import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRoleRepository extends JpaRepository<CategoryRole, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT cr "
            + "FROM CategoryRole cr "
            + "WHERE cr.member.id = :memberId AND cr.category.id = :categoryId")
    Optional<CategoryRole> findByMemberIdAndCategoryId(final Long memberId, final Long categoryId);

    @EntityGraph(attributePaths = {"member"})
    List<CategoryRole> findByCategoryId(final Long categoryId);

    @EntityGraph(attributePaths = {"category", "category.member"})
    List<CategoryRole> findByMemberId(final Long memberId);

    @Query("SELECT count(cr) "
            + "FROM CategoryRole cr "
            + "WHERE cr.categoryRoleType = :categoryRoleType "
            + "AND cr.member.id = :memberId")
    int countByMemberIdAndCategoryRoleType(final Long memberId, final CategoryRoleType categoryRoleType);

    int countByCategoryIdAndCategoryRoleType(final Long categoryId, final CategoryRoleType categoryRoleType);

    void deleteByCategoryId(final Long categoryId);

    default CategoryRole getByMemberIdAndCategoryId(final Long memberId, final Long categoryId) {
        return findByMemberIdAndCategoryId(memberId, categoryId)
                .orElseThrow(NoSuchCategoryRoleException::new);
    }

    default boolean isMemberSoleAdminInCategory(final Long memberId, final Long categoryId) {
        CategoryRole categoryRole = getByMemberIdAndCategoryId(memberId, categoryId);
        int adminCount = countByCategoryIdAndCategoryRoleType(categoryId, CategoryRoleType.ADMIN);

        return categoryRole.isAdmin() && adminCount == 1;
    }

    default void validateManagingCategoryLimit(final Long memberId, final CategoryRoleType categoryRoleType) {
        int memberAdminCount = countByMemberIdAndCategoryRoleType(memberId, ADMIN);

        if (!categoryRoleType.equals(NONE) && memberAdminCount >= CategoryRole.MAX_MANAGING_CATEGORY_COUNT) {
            throw new ManagingCategoryLimitExcessException();
        }
    }
}
