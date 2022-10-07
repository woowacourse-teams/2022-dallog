package com.allog.dallog.domain.categoryrole.domain;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.categoryrole.exception.NoCategoryAuthorityException;
import com.allog.dallog.domain.common.BaseEntity;
import com.allog.dallog.domain.member.domain.Member;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "category_roles")
@Entity
public class CategoryRole extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categories_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private CategoryRoleType categoryRoleType;

    protected CategoryRole() {
    }

    public CategoryRole(final Category category, final Member member, final CategoryRoleType categoryRoleType) {
        this.category = category;
        this.member = member;
        this.categoryRoleType = categoryRoleType;
    }

    public boolean isAdmin() {
        return categoryRoleType.equals(CategoryRoleType.ADMIN);
    }

    public boolean isNone() {
        return categoryRoleType.equals(CategoryRoleType.NONE);
    }

    public void validateAuthority(final CategoryAuthority authority) {
        if (!ableTo(authority)) {
            throw new NoCategoryAuthorityException(authority.getName());
        }
    }

    public boolean ableTo(final CategoryAuthority authority) {
        return categoryRoleType.ableTo(authority);
    }

    public void changeRole(final CategoryRoleType categoryRoleType) {
        this.categoryRoleType = categoryRoleType;
    }

    public Long getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public Member getMember() {
        return member;
    }

    public CategoryRoleType getCategoryRoleType() {
        return categoryRoleType;
    }
}
