package com.allog.dallog.category.domain;

import com.allog.dallog.category.exception.InvalidCategoryException;
import com.allog.dallog.global.domain.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    protected Category() {
    }

    public Category(final String name) {
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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
