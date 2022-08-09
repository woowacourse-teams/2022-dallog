package com.allog.dallog.domain.category.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c "
            + "FROM Category c "
            + "WHERE c.name LIKE %:name% AND c.isPrivate = FALSE")
    Slice<Category> findAllLikeCategoryName(final String name, final Pageable pageable);

    Slice<Category> findSliceByMemberId(final Long memberId, final Pageable pageable);

    boolean existsByIdAndMemberId(Long id, Long memberId);
}
