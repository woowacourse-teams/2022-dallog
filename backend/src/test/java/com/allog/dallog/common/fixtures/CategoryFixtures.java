package com.allog.dallog.common.fixtures;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.dto.MemberResponse;
import java.time.LocalDateTime;

public class CategoryFixtures {

    /* 공통 일정 카테고리 */
    public static final String 공통_일정_이름 = "공통 일정";
    public static final CategoryCreateRequest 공통_일정_생성_요청 = new CategoryCreateRequest(공통_일정_이름, false);

    /* BE 일정 카테고리 */
    public static final String BE_일정_이름 = "BE 일정";
    public static final CategoryCreateRequest BE_일정_생성_요청 = new CategoryCreateRequest(BE_일정_이름, false);

    /* FE 일정 카테고리 */
    public static final String FE_일정_이름 = "FE 일정";
    public static final CategoryCreateRequest FE_일정_생성_요청 = new CategoryCreateRequest(FE_일정_이름, false);

    /* 매트 아고라 카테고리 */
    public static final String 매트_아고라_이름 = "매트 아고라";
    public static final CategoryCreateRequest 매트_아고라_생성_요청 = new CategoryCreateRequest(매트_아고라_이름, false);

    /* 후디 JPA 스터디 카테고리 */
    public static final String 후디_JPA_스터디_이름 = "후디 JPA 스터디";
    public static final CategoryCreateRequest 후디_JPA_스터디_생성_요청 = new CategoryCreateRequest(후디_JPA_스터디_이름, false);

    /* 후디 개인 학습 일정 카테고리 */
    public static final String 후디_개인_학습_일정_이름 = "후디 개인 학습 일정";
    public static final CategoryCreateRequest 후디_개인_학습_일정_생성_요청 = new CategoryCreateRequest(후디_개인_학습_일정_이름, true);

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

    public static Category 후디_개인_학습_일정(final Member creator) {
        return new Category(후디_개인_학습_일정_이름, creator);
    }

    public static CategoryResponse 공통_일정_응답(final MemberResponse creatorResponse) {
        return new CategoryResponse(1L, 공통_일정_이름, creatorResponse, LocalDateTime.now());
    }

    public static CategoryResponse BE_일정_응답(final MemberResponse creatorResponse) {
        return new CategoryResponse(2L, BE_일정_이름, creatorResponse, LocalDateTime.now());
    }

    public static CategoryResponse FE_일정_응답(final MemberResponse creatorResponse) {
        return new CategoryResponse(3L, FE_일정_이름, creatorResponse, LocalDateTime.now());
    }

    public static CategoryResponse 매트_아고라_응답(final MemberResponse creatorResponse) {
        return new CategoryResponse(4L, 매트_아고라_이름, creatorResponse, LocalDateTime.now());
    }

    public static CategoryResponse 후디_JPA_스터디_응답(final MemberResponse creatorResponse) {
        return new CategoryResponse(5L, 후디_JPA_스터디_이름, creatorResponse, LocalDateTime.now());
    }
}
