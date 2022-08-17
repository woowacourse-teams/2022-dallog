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
    Slice<Category> findAllLikeCategoryNameAndCategoryType(final String name, final CategoryType categoryType, final Pageable pageable);

    @Query("SELECT c "
            + "FROM Category c "
            + "WHERE c.member.id = :memberId AND c.name LIKE %:name%")
    Slice<Category> findByMemberIdLikeCategoryName(final Long memberId, final String name, final Pageable pageable);

    List<Category> findByMemberId(Long memberId);

    boolean existsByIdAndMemberId(Long id, Long memberId);

    void deleteByMemberId(Long memberId);
}
