package com.allog.dallog.common.fixtures;

import com.allog.dallog.domain.category.dto.request.ExternalCategoryCreateRequest;

public class ExternalCategoryFixtures {

    public static final String 대한민국_공휴일_이름 = "대한민국 공휴일";
    public static final String 우아한테크코스_이름 = "우아한테크코스";
    public static final String 내_일정_이름 = "내 일정";

    public static final ExternalCategoryCreateRequest 대한민국_공휴일_생성_요청 = new ExternalCategoryCreateRequest(
            "ko.south_korea#holiday@group.v.calendar.google.com", 대한민국_공휴일_이름);

    public static final ExternalCategoryCreateRequest 우아한테크코스_생성_요청 = new ExternalCategoryCreateRequest(
            "en.south_korea#holiday@group.v.calendar.google.com", 우아한테크코스_이름);

    public static final ExternalCategoryCreateRequest 내_일정_생성_요청 = new ExternalCategoryCreateRequest(
            "example@email.com", 내_일정_이름);
}
