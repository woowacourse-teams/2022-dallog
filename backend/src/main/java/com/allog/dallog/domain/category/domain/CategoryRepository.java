package com.allog.dallog.domain.category.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Slice<Category> findSliceBy(final Pageable pageable);

    @Query("SELECT c "
            + "FROM Category c "
            + "WHERE c.name LIKE %:name%")
    Slice<Category> findAllLikeCategoryName(final Pageable pageable, final String name);

    Slice<Category> findSliceByMemberId(final Pageable pageable, final Long memberId);

    boolean existsByIdAndMemberId(Long id, Long memberId);
}
