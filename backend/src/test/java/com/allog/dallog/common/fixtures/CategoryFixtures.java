package com.allog.dallog.common.fixtures;

import static com.allog.dallog.common.fixtures.MemberFixtures.CREATOR;
import static com.allog.dallog.common.fixtures.MemberFixtures.MEMBER;

import com.allog.dallog.category.domain.Category;
import com.allog.dallog.category.dto.request.CategoryCreateRequest;

public class CategoryFixtures {

    public static final String CATEGORY_NAME = "달록";

    public static final int PAGE_NUMBER = 1;
    public static final int PAGE_SIZE = 2;

    public static final Category CATEGORY = new Category(CATEGORY_NAME, MEMBER);

    public static final Category CATEGORY_1 = new Category("BE 일정", CREATOR);
    public static final Category CATEGORY_2 = new Category("FE 일정", CREATOR);
    public static final Category CATEGORY_3 = new Category("공통 일정", CREATOR);

    public static final CategoryCreateRequest CATEGORY_CREATE_REQUEST_1 = new CategoryCreateRequest("BE 일정");
    public static final CategoryCreateRequest CATEGORY_CREATE_REQUEST_2 = new CategoryCreateRequest("FE 일정");
    public static final CategoryCreateRequest CATEGORY_CREATE_REQUEST_3 = new CategoryCreateRequest("공통 일정");
}
