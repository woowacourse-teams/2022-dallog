package com.allog.dallog.domain.category.domain;

import com.allog.dallog.domain.category.exception.NoSuchExternalCategoryDetailException;
import com.allog.dallog.domain.category.exception.ExistExternalCategoryException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExternalCategoryDetailRepository extends JpaRepository<ExternalCategoryDetail, Long> {

    Optional<ExternalCategoryDetail> findByCategoryId(final Long categoryId);

    boolean existsByExternalIdAndCategoryIn(final String externalId, final List<Category> categories);

    void deleteByCategoryId(final Long categoryId);

    default ExternalCategoryDetail getByCategoryId(final Long categoryId) {
        return this.findByCategoryId(categoryId)
                .orElseThrow(NoSuchExternalCategoryDetailException::new);
    }

    default void validateExistByExternalIdAndCategoryIn(final String externalId, final List<Category> externalCategories) {
        if (existsByExternalIdAndCategoryIn(externalId, externalCategories)) {
            throw new ExistExternalCategoryException();
        }
    }
}
