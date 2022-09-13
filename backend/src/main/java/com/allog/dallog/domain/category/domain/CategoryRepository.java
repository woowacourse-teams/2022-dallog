package com.allog.dallog.domain.category.domain;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c "
            + "FROM Category c "
            + "WHERE c.name LIKE %:name% AND c.categoryType = :categoryType")
    Slice<Category> findByNameContainingAndCategoryType(final String name, final CategoryType categoryType, final Pageable pageable);

    List<Category> findByMemberId(final Long memberId);

    void deleteByMemberId(final Long memberId);

    boolean existsByIdAndMemberId(final Long id, final Long memberId);
}
