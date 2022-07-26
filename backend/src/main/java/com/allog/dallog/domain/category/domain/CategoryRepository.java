package com.allog.dallog.domain.category.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Slice<Category> findSliceBy(final Pageable pageable);

    Slice<Category> findSliceByMemberId(final Pageable pageable, final Long memberId);

    boolean existsByIdAndMemberId(Long id, Long memberId);
}
