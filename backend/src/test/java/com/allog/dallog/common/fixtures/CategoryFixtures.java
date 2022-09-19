package com.allog.dallog.common.fixtures;

import static com.allog.dallog.domain.category.domain.CategoryType.GOOGLE;
import static com.allog.dallog.domain.category.domain.CategoryType.NORMAL;
import static com.allog.dallog.domain.category.domain.CategoryType.PERSONAL;

import com.allog.dallog.domain.category.domain.Category;
import com.allog.dallog.domain.category.dto.request.CategoryCreateRequest;
import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.member.domain.Member;
import com.allog.dallog.domain.member.dto.MemberResponse;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class CategoryFixtures {

    /* 공통 일정 카테고리 */
    public static final String 공통_일정_이름 = "공통 일정";
    public static final CategoryCreateRequest 공통_일정_생성_요청 = new CategoryCreateRequest(공통_일정_이름, NORMAL);

    /* BE 일정 카테고리 */
    public static final String BE_일정_이름 = "BE 일정";
    public static final CategoryCreateRequest BE_일정_생성_요청 = new CategoryCreateRequest(BE_일정_이름, NORMAL);
    public static final CategoryCreateRequest 외부_BE_일정_생성_요청 = new CategoryCreateRequest(BE_일정_이름, GOOGLE);

    /* FE 일정 카테고리 */
    public static final String FE_일정_이름 = "FE 일정";
    public static final CategoryCreateRequest FE_일정_생성_요청 = new CategoryCreateRequest(FE_일정_이름, NORMAL);
    public static final CategoryCreateRequest 외부_FE_일정_생성_요청 = new CategoryCreateRequest(FE_일정_이름, GOOGLE);

    /* 매트 아고라 카테고리 */
    public static final String 매트_아고라_이름 = "매트 아고라";
    public static final CategoryCreateRequest 매트_아고라_생성_요청 = new CategoryCreateRequest(매트_아고라_이름, NORMAL);

    /* 후디 JPA 스터디 카테고리 */
    public static final String 후디_JPA_스터디_이름 = "후디 JPA 스터디";
    public static final CategoryCreateRequest 후디_JPA_스터디_생성_요청 = new CategoryCreateRequest(후디_JPA_스터디_이름, NORMAL);

    /* 내 일정 카테고리 */
    public static final String 내_일정_이름 = "내 일정";
    public static final CategoryCreateRequest 내_일정_생성_요청 = new CategoryCreateRequest(내_일정_이름, PERSONAL);

    /* 우아한테크코스 외부 일정 카테고리 */
    public static final String 우아한테크코스_이름 = "우아한테크코스";
    public static final CategoryCreateRequest 우아한테크코스_외부_일정_생성_요청 = new CategoryCreateRequest(우아한테크코스_이름, GOOGLE);

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

    public static Category 내_일정(final Member creator) {
        return new Category(내_일정_이름, creator, PERSONAL);
    }

    public static Category 우아한테크코스_일정(final Member creator) {
        return new Category(우아한테크코스_이름, creator, GOOGLE);
    }

    public static CategoryResponse 공통_일정_응답(final MemberResponse creatorResponse) {
        return new CategoryResponse(1L, 공통_일정_이름, NORMAL.name(), creatorResponse, LocalDateTime.now());
    }

    public static CategoryResponse BE_일정_응답(final MemberResponse creatorResponse) {
        return new CategoryResponse(2L, BE_일정_이름, NORMAL.name(), creatorResponse, LocalDateTime.now());
    }

    public static CategoryResponse FE_일정_응답(final MemberResponse creatorResponse) {
        return new CategoryResponse(3L, FE_일정_이름, NORMAL.name(), creatorResponse, LocalDateTime.now());
    }

    public static CategoryResponse 매트_아고라_응답(final MemberResponse creatorResponse) {
        return new CategoryResponse(4L, 매트_아고라_이름, NORMAL.name(), creatorResponse, LocalDateTime.now());
    }

    public static CategoryResponse 후디_JPA_스터디_응답(final MemberResponse creatorResponse) {
        return new CategoryResponse(5L, 후디_JPA_스터디_이름, NORMAL.name(), creatorResponse, LocalDateTime.now());
    }

    public static Category setId(final Category category, final Long id) {
        try {
            Field idField = Category.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(category, id);
            return category;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
