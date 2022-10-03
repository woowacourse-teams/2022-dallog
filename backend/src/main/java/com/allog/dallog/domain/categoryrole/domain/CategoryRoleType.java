package com.allog.dallog.domain.categoryrole.domain;

import static com.allog.dallog.domain.categoryrole.domain.CategoryAuthority.ADD_SCHEDULE;
import static com.allog.dallog.domain.categoryrole.domain.CategoryAuthority.DELETE_CATEGORY;
import static com.allog.dallog.domain.categoryrole.domain.CategoryAuthority.DELETE_SCHEDULE;
import static com.allog.dallog.domain.categoryrole.domain.CategoryAuthority.MANAGE_ROLE;
import static com.allog.dallog.domain.categoryrole.domain.CategoryAuthority.UPDATE_CATEGORY;
import static com.allog.dallog.domain.categoryrole.domain.CategoryAuthority.UPDATE_SCHEDULE;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

public enum CategoryRoleType {
    ADMIN(EnumSet.of(UPDATE_CATEGORY, DELETE_CATEGORY, ADD_SCHEDULE, UPDATE_SCHEDULE, DELETE_SCHEDULE, MANAGE_ROLE)),
    NONE(Set.of());

    private final Set<CategoryAuthority> authorities;

    CategoryRoleType(final Set<CategoryAuthority> authorities) {
        this.authorities = authorities;
    }

    public static Set<CategoryRoleType> getHavingAuthorities(final Set<CategoryAuthority> authorities) {
        return Arrays.stream(values())
                .filter(categoryRoleType -> categoryRoleType.authorities.containsAll(authorities))
                .collect(Collectors.toSet());
    }

    public boolean ableTo(final CategoryAuthority authority) {
        return authorities.contains(authority);
    }
}
