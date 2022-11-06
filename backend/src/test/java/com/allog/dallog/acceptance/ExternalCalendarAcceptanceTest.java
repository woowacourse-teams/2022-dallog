package com.allog.dallog.acceptance;

import static com.allog.dallog.common.fixtures.AuthFixtures.GOOGLE_PROVIDER;
import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_MEMBER_인증_코드;

import com.allog.dallog.acceptance.builder.AuthAssuredBuilder;
import com.allog.dallog.acceptance.builder.AuthAssuredBuilder.AuthResponseBuilder;
import com.allog.dallog.acceptance.builder.ExternalCalendarAssuredBuilder;
import com.allog.dallog.domain.category.dto.request.ExternalCategoryCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("외부 캘린더 관련 기능")
class ExternalCalendarAcceptanceTest extends AcceptanceTest {

    public final ExternalCategoryCreateRequest 외부_캘린더_추가_요청 = new ExternalCategoryCreateRequest(
            "en.south_korea#holiday@group.v.calendar.google.com", "외부 캘린더");

    @DisplayName("자신의 외부 캘린더 리스트를 조회한다.")
    @Test
    void 자신의_외부_캘린더_리스트를_조회한다() {
        AuthResponseBuilder 회원 = AuthAssuredBuilder.요청()
                .회원가입_한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드)
                .응답()
                .상태코드_200을_받는다()
                .토큰들을_발급_받는다();

        ExternalCalendarAssuredBuilder.요청()
                .자신의_외부_캘린더_목록을_조회한다(회원.accessToken())
                .응답()
                .상태코드_200을_받는다()
                .자신의_외부_캘린더_목록을_받는다(3);
    }

    @DisplayName("외부 캘린더를 추가하면 201을 반환한다.")
    @Test
    void 외부_캘린더를_추가하면_201을_반환한다() {
        AuthResponseBuilder 회원 = AuthAssuredBuilder.요청()
                .회원가입_한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드)
                .응답()
                .상태코드_200을_받는다()
                .토큰들을_발급_받는다();

        ExternalCalendarAssuredBuilder.요청()
                .외부_캘린더를_추가한다(회원.accessToken(), 외부_캘린더_추가_요청)
                .응답()
                .상태코드_201을_받는다();
    }
}
