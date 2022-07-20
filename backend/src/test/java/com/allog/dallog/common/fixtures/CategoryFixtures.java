package com.allog.dallog.common.fixtures;

import static com.allog.dallog.common.fixtures.MemberFixtures.MEMBER;

import com.allog.dallog.category.domain.Category;

public class CategoryFixtures {

    public static final String CATEGORY_NAME = "달록";

    public static final int PAGE_NUMBER = 1;
    public static final int PAGE_SIZE = 2;

    public static final Category CATEGORY = new Category(CATEGORY_NAME, MEMBER);
}
