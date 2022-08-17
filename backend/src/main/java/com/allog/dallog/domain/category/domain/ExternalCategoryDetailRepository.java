package com.allog.dallog.domain.category.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExternalCategoryDetailRepository extends JpaRepository<ExternalCategoryDetail, Long> {

    Optional<ExternalCategoryDetail> findByCategoryId(final Long categoryId);

    boolean existsByExternalIdAndCategoryIn(final String externalId, final List<Category> categories);

    void deleteByCategoryId(final Long categoryId);

    boolean existsByCategoryId(final Long categoryId);
}
