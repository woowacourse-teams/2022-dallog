package com.allog.dallog.common.fixtures;

import com.allog.dallog.category.domain.Category;
import com.allog.dallog.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.member.domain.Member;
import com.allog.dallog.member.domain.SocialType;

public class CategoryFixtures {

    /* 공통 일정 카테고리 */
    public static final String 공통_일정_이름 = "공통 일정";
    public static final CategoryCreateRequest 공통_일정_생성_요청 = new CategoryCreateRequest(공통_일정_이름);

    /* BE 일정 카테고리 */
    public static final String BE_일정_이름 = "BE 일정";
    public static final CategoryCreateRequest BE_일정_생성_요청 = new CategoryCreateRequest(BE_일정_이름);

    /* FE 일정 카테고리 */
    public static final String FE_일정_이름 = "BE 일정";
    public static final CategoryCreateRequest FE_일정_생성_요청 = new CategoryCreateRequest(FE_일정_이름);

    /* 매트 아고라 카테고리 */
    public static final String 매트_아고라_이름 = "매트 아고라";
    public static final CategoryCreateRequest 매트_아고라_생성_요청 = new CategoryCreateRequest(매트_아고라_이름);

    /* 후디 JPA 스터디 카테고리 */
    public static final String 후디_JPA_스터디_이름 = "후디 JPA 스터디";
    public static final CategoryCreateRequest 후디_JPA_스터디_생성_요청 = new CategoryCreateRequest(후디_JPA_스터디_이름);

    public static Category 공통_일정(final Member creator) {
        return new Category(공통_일정_이름, creator);
    }

    public static Category BE_일정(final Member creator) {
        return new Category(BE_일정_이름, creator);
    }

    public static Category FE_일정(final Member creator) {
        return new Category(FE_일정_이름, creator);
    }

    public static Category 매트_아고라(final Member creator) {
        return new Category(매트_아고라_이름, creator);
    }

    public static Category 후디_JPA_스터디(final Member creator) {
        return new Category(후디_JPA_스터디_이름, creator);
    }

    /* 임시 멤버 */
    public static Member 후디() {
        return new Member("devhudi@gmail.com", "/image.png", "후디", SocialType.GOOGLE);
    }

    public static Member 매트() {
        return new Member("dev.hyeonic@gmail.com", "/image2.png", "매트", SocialType.GOOGLE);
    }

    public static Member 관리자() {
        return new Member("dallog.admin@gmail.com", "/image3.png", "관리자", SocialType.GOOGLE);
    }
}
