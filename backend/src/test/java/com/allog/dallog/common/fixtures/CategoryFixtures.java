package com.allog.dallog.common.fixtures;

import static com.allog.dallog.common.fixtures.MemberFixtures.MEMBER;

import com.allog.dallog.category.domain.Category;

public class CategoryFixtures {

    public static final String CATEGORY_NAME = "달록";

    public static final int PAGE_NUMBER = 1;
    public static final int PAGE_SIZE = 2;

<<<<<<< HEAD
    public static final Category CATEGORY = new Category(CATEGORY_NAME, MEMBER);
=======
    public static final Category CATEGORY = new Category(CATEGORY_NAME);

    public static final Category CATEGORY_1 = new Category("BE 일정");
    public static final Category CATEGORY_2 = new Category("FE 일정");
    public static final Category CATEGORY_3 = new Category("공통 일정");
>>>>>>> e056d17 (test: SubscriptionRepository 검증 추가)
}
