package com.allog.dallog.domain.category.domain;

import com.allog.dallog.domain.category.exception.NoSuchCategoryException;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c "
            + "FROM Subscription s "
            + "JOIN s.category c "
            + "WHERE c.categoryType = :categoryType AND c.name LIKE %:name% "
            + "GROUP BY c.id "
            + "ORDER BY COUNT(c.id) DESC")
    List<Category> findByCategoryTypeAndNameContaining(final CategoryType categoryType, final String name);

    @Query("SELECT c "
            + "FROM Category c "
            + "WHERE c.member.id = :memberId AND c.categoryType = :categoryType")
    List<Category> findByMemberIdAndCategoryType(final Long memberId, final CategoryType categoryType);

    @Query("SELECT c "
            + "FROM Category c "
            + "WHERE c.member.id = :memberId")
    List<Category> findByMemberId(final Long memberId);

    default Category getById(final Long id) {
        return this.findById(id)
                .orElseThrow(NoSuchCategoryException::new);
    }
}
