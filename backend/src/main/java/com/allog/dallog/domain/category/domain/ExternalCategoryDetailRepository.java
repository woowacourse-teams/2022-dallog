package com.allog.dallog.domain.category.domain;

import com.allog.dallog.domain.category.exception.ExistExternalCategoryException;
import com.allog.dallog.domain.category.exception.NoSuchExternalCategoryDetailException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExternalCategoryDetailRepository extends JpaRepository<ExternalCategoryDetail, Long> {

    Optional<ExternalCategoryDetail> findByCategory(final Category category);

    List<ExternalCategoryDetail> findByCategoryIn(final List<Category> categories);

    boolean existsByExternalIdAndCategoryIn(final String externalId, final List<Category> categories);

    void deleteByCategoryId(final Long categoryId);

    default ExternalCategoryDetail getByCategory(final Category category) {
        return this.findByCategory(category)
                .orElseThrow(NoSuchExternalCategoryDetailException::new);
    }

    default void validateExistByExternalIdAndCategoryIn(final String externalId,
                                                        final List<Category> externalCategories) {
        if (existsByExternalIdAndCategoryIn(externalId, externalCategories)) {
            throw new ExistExternalCategoryException();
        }
    }
}
