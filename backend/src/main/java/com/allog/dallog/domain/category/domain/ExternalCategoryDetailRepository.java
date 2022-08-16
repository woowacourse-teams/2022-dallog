package com.allog.dallog.domain.category.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExternalCategoryDetailRepository extends JpaRepository<ExternalCategoryDetail, Long> {

    Optional<ExternalCategoryDetail> findByCategoryId(final Long categoryId);
}
