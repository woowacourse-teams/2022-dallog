package com.allog.dallog.acceptance;

import static com.allog.dallog.acceptance.fixtures.AuthAcceptanceFixtures.자체_토큰을_생성하고_토큰을_반환한다;
import static com.allog.dallog.acceptance.fixtures.CategoryAcceptanceFixtures.새로운_카테고리를_등록한다;
import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_200이_반환된다;
import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_201이_반환된다;
import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_204가_반환된다;
import static com.allog.dallog.acceptance.fixtures.ScheduleAcceptanceFixtures.새로운_일정을_등록한다;
import static com.allog.dallog.acceptance.fixtures.ScheduleAcceptanceFixtures.일정_아이디로_일정을_단건_조회한다;
import static com.allog.dallog.acceptance.fixtures.ScheduleAcceptanceFixtures.일정을_삭제한다;
import static com.allog.dallog.acceptance.fixtures.ScheduleAcceptanceFixtures.일정을_수정한다;
import static com.allog.dallog.common.fixtures.AuthFixtures.GOOGLE_PROVIDER;
import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_MEMBER_인증_코드;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_생성_요청;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.레벨_인터뷰_메모;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.레벨_인터뷰_시작일시;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.레벨_인터뷰_제목;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.레벨_인터뷰_종료일시;

import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.schedule.dto.request.ScheduleUpdateRequest;
import com.allog.dallog.domain.schedule.dto.response.ScheduleResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("일정 관련 기능")
class ScheduleAcceptanceTest extends AcceptanceTest {

    @DisplayName("정상적인 일정정보를 등록하면 상태코드 201을 반환한다.")
    @Test
    void 정상적인_일정정보를_등록하면_상태코드_201을_반환한다() {
        // given
        String accessToken = 자체_토큰을_생성하고_토큰을_반환한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드);
        CategoryResponse 공통_일정_응답 = 새로운_카테고리를_등록한다(accessToken, 공통_일정_생성_요청).as(CategoryResponse.class);

        // when
        ExtractableResponse<Response> response = 새로운_일정을_등록한다(accessToken, 공통_일정_응답.getId());

        // then
        상태코드_201이_반환된다(response);
    }

    @DisplayName("일정 ID로 일정을 단건조회_하면 상태코드 200을 반환한다.")
    @Test
    void 일정_ID로_일정을_단건조회_하면_상태코드_200을_반환한다() {
        // given
        String accessToken = 자체_토큰을_생성하고_토큰을_반환한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드);
        CategoryResponse 공통_일정_응답 = 새로운_카테고리를_등록한다(accessToken, 공통_일정_생성_요청).as(CategoryResponse.class);
        ScheduleResponse 알록달록_회의 = 새로운_일정을_등록한다(accessToken, 공통_일정_응답.getId()).as(ScheduleResponse.class);

        // when
        ExtractableResponse<Response> response = 일정_아이디로_일정을_단건_조회한다(accessToken, 알록달록_회의.getId());

        // then
        상태코드_200이_반환된다(response);
    }

    @DisplayName("일정을 수정하면 상태코드 204를 반환한다.")
    @Test
    void 일정을_수정하면_상태코드_204를_반환한다() {
        // given
        String accessToken = 자체_토큰을_생성하고_토큰을_반환한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드);
        CategoryResponse 공통_일정_응답 = 새로운_카테고리를_등록한다(accessToken, 공통_일정_생성_요청).as(CategoryResponse.class);
        ScheduleResponse 알록달록_회의 = 새로운_일정을_등록한다(accessToken, 공통_일정_응답.getId()).as(ScheduleResponse.class);

        ScheduleUpdateRequest 일정_수정_요청 = new ScheduleUpdateRequest(공통_일정_응답.getId(), 레벨_인터뷰_제목, 레벨_인터뷰_시작일시,
                레벨_인터뷰_종료일시, 레벨_인터뷰_메모);

        // when
        ExtractableResponse<Response> response = 일정을_수정한다(accessToken, 알록달록_회의.getId(), 일정_수정_요청);

        // then
        상태코드_204가_반환된다(response);
    }

    @DisplayName("일정을 삭제하면 상태코드 204를 반환한다.")
    @Test
    void 일정을_삭제하면_상태코드_204를_반환한다() {
        // given
        String accessToken = 자체_토큰을_생성하고_토큰을_반환한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드);
        CategoryResponse 공통_일정_응답 = 새로운_카테고리를_등록한다(accessToken, 공통_일정_생성_요청).as(CategoryResponse.class);
        ScheduleResponse 알록달록_회의 = 새로운_일정을_등록한다(accessToken, 공통_일정_응답.getId()).as(ScheduleResponse.class);

        // when
        ExtractableResponse<Response> response = 일정을_삭제한다(accessToken, 알록달록_회의.getId());

        // then
        상태코드_204가_반환된다(response);
    }
}
