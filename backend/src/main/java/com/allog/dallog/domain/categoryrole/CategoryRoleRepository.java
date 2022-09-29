package com.allog.dallog.domain.categoryrole;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRoleRepository extends JpaRepository<CategoryRole, Long> {

    Optional<CategoryRole> findByMemberIdAndCategoryId(final Long memberId, final Long categoryId);
}
