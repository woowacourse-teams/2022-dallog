package com.allog.dallog.domain.category.domain;

import com.allog.dallog.domain.auth.exception.NoPermissionException;
import com.allog.dallog.domain.category.exception.NoSuchCategoryException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c "
            + "FROM Category c "
            + "WHERE c.member.id = :memberId AND c.name LIKE %:name%")
    Slice<Category> findByMemberIdAndNameContaining(final Long memberId, final String name, final Pageable pageable);

    @Query("SELECT c "
            + "FROM Category c "
            + "WHERE c.name LIKE %:name% AND c.categoryType = :categoryType")
    Slice<Category> findByNameContainingAndCategoryType(final String name, final CategoryType categoryType,
                                                        final Pageable pageable);

    List<Category> findByMemberIdAndCategoryType(final Long memberId, final CategoryType categoryType);

    List<Category> findByMemberId(final Long memberId);

    boolean existsByIdAndMemberId(final Long id, final Long memberId);

    void deleteByMemberId(final Long memberId);

    default Category getById(final Long id) {
        return this.findById(id)
                .orElseThrow(NoSuchCategoryException::new);
    }

    default void validateExistsByIdAndMemberId(final Long id, final Long memberId) {
        if (!existsByIdAndMemberId(id, memberId)) {
            throw new NoPermissionException();
        }
    }
}
