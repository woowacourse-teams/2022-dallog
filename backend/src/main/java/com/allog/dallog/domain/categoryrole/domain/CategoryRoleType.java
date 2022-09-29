package com.allog.dallog.domain.categoryrole.domain;

import static com.allog.dallog.domain.categoryrole.domain.CategoryAuthority.ADD_SCHEDULE;
import static com.allog.dallog.domain.categoryrole.domain.CategoryAuthority.DELETE_CATEGORY;
import static com.allog.dallog.domain.categoryrole.domain.CategoryAuthority.DELETE_SCHEDULE;
import static com.allog.dallog.domain.categoryrole.domain.CategoryAuthority.MANAGE_ROLE;
import static com.allog.dallog.domain.categoryrole.domain.CategoryAuthority.MODIFY_SCHEDULE;
import static com.allog.dallog.domain.categoryrole.domain.CategoryAuthority.RENAME_CATEGORY;

import java.util.Set;

public enum CategoryRoleType {
    ADMIN(Set.of(RENAME_CATEGORY, DELETE_CATEGORY, ADD_SCHEDULE, MODIFY_SCHEDULE, DELETE_SCHEDULE, MANAGE_ROLE)),
    NONE(Set.of());

    private final Set<CategoryAuthority> authorities;

    CategoryRoleType(final Set<CategoryAuthority> authorities) {
        this.authorities = authorities;
    }

    public boolean ableTo(final CategoryAuthority authority) {
        return authorities.stream()
                .anyMatch(it -> it.equals(authority));
    }
}
