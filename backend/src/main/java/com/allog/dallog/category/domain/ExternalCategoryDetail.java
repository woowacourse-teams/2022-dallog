package com.allog.dallog.category.domain;

import com.allog.dallog.common.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name = "external_category_details")
@Entity
public class ExternalCategoryDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categories_id", nullable = false)
    private Category category;

    @Column(name = "external_id", nullable = false)
    private String externalId;

    protected ExternalCategoryDetail() {
    }

    public ExternalCategoryDetail(final Category category, final String externalId) {
        this.category = category;
        this.externalId = externalId;
    }

    public Long getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public String getExternalId() {
        return externalId;
    }
}
