package com.allog.dallog.domain.category.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExternalCategoryRepository extends JpaRepository<ExternalCategoryDetail, Long> {
}
