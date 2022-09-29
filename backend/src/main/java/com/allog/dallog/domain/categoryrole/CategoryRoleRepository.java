package com.allog.dallog.domain.categoryrole;

import com.allog.dallog.domain.categoryrole.exception.NoSuchCategoryRoleException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRoleRepository extends JpaRepository<CategoryRole, Long> {

    Optional<CategoryRole> findByMemberIdAndCategoryId(final Long memberId, final Long categoryId);

    void deleteByCategoryId(final Long categoryId);

    default CategoryRole getByMemberIdAndCategoryId(final Long memberId, final Long categoryId) {
        return findByMemberIdAndCategoryId(memberId, categoryId)
                .orElseThrow(NoSuchCategoryRoleException::new);
    }
}
