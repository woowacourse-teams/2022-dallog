package com.allog.dallog.category.domain;

import com.allog.dallog.auth.exception.NoPermissionException;
import com.allog.dallog.category.exception.InvalidCategoryException;
import com.allog.dallog.common.BaseEntity;
import com.allog.dallog.member.domain.Member;
import javax.persistence.Column;
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

@Table(name = "categories")
@Entity
public class Category extends BaseEntity {

    public static final int MAX_NAME_LENGTH = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id")
    private Member member;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "category_type", nullable = false)
    private CategoryType categoryType;

    protected Category() {
    }

    public Category(final String name, final Member member) {
        validateNameLength(name);
        this.name = name;
        this.member = member;
        this.categoryType = CategoryType.NORMAL;
    }

    public Category(final String name, final Member member, final CategoryType categoryType) {
        validateNameLength(name);
        this.name = name;
        this.member = member;
        this.categoryType = categoryType;
    }

    public void changeName(final String name) {
        validatePersonal();
        validateNameLength(name);
        this.name = name;
    }

    private void validatePersonal() {
        if (isPersonal()) {
            throw new InvalidCategoryException("'내 일정' 카테고리는 수정할 수 없습니다.");
        }
    }

    private void validateNameLength(final String name) {
        if (name.isBlank()) {
            throw new InvalidCategoryException("카테고리 이름은 공백일 수 없습니다.");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new InvalidCategoryException(String.format("카테고리 이름의 길이는 %d을 초과할 수 없습니다.", MAX_NAME_LENGTH));
        }
    }

    public void validateSubscriptionPossible(final Member member) {
        if (this.categoryType == CategoryType.PERSONAL && !isCreatorId(member.getId())) {
            throw new NoPermissionException("구독 권한이 없는 카테고리입니다.");
        }
    }

    public void validateNotExternalCategory() {
        if (categoryType == CategoryType.GOOGLE) {
            throw new NoPermissionException("외부 연동 카테고리에는 일정을 추가할 수 없습니다.");
        }
    }

    public boolean isCreatorId(final Long creatorId) {
        return member.hasSameId(creatorId);
    }

    public boolean isNormal() {
        return categoryType == CategoryType.NORMAL;
    }

    public boolean isPersonal() {
        return categoryType == CategoryType.PERSONAL;
    }

    public boolean isInternal() {
        return categoryType != CategoryType.GOOGLE;
    }

    public boolean isExternal() {
        return categoryType == CategoryType.GOOGLE;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(final Member member) {
        this.member = member;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }
}
