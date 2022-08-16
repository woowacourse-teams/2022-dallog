package com.allog.dallog.domain.category.domain;

import static com.allog.dallog.domain.category.domain.CategoryType.PERSONAL;

import com.allog.dallog.domain.category.exception.InvalidCategoryException;
import com.allog.dallog.domain.common.BaseEntity;
import com.allog.dallog.domain.member.domain.Member;
import java.util.Objects;
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
    @JoinColumn(name = "members_id", nullable = false)
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
        validateNameLength(name);
        this.name = name;
    }

    private void validateNameLength(final String name) {
        if (name.isBlank()) {
            throw new InvalidCategoryException("카테고리 이름은 공백일 수 없습니다.");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new InvalidCategoryException("카테고리 이름의 길이는 20을 초과할 수 없습니다.");
        }
    }

    public boolean isCreator(final Long memberId) {
        return Objects.equals(member.getId(), memberId);
    }

    public boolean isPersonal() {
        return categoryType == PERSONAL;
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

    public CategoryType getCategoryType() {
        return categoryType;
    }
}
