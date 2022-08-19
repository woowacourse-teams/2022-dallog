package com.allog.dallog.acceptance;

import static com.allog.dallog.acceptance.fixtures.AuthAcceptanceFixtures.자체_토큰을_생성하고_토큰을_반환한다;
import static com.allog.dallog.acceptance.fixtures.CategoryAcceptanceFixtures.새로운_카테고리를_등록한다;
import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_200이_반환된다;
import static com.allog.dallog.acceptance.fixtures.ScheduleAcceptanceFixtures.새로운_일정을_등록한다;
import static com.allog.dallog.common.fixtures.AuthFixtures.GOOGLE_PROVIDER;
import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_MEMBER_인증_코드;
import static com.allog.dallog.common.fixtures.CategoryFixtures.공통_일정_생성_요청;

import com.allog.dallog.domain.category.dto.response.CategoryResponse;
import com.allog.dallog.domain.schedule.dto.response.ScheduleResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("일정 관련 기능")
public class SchedulerAcceptanceTest extends AcceptanceTest {

    @DisplayName("일정 조율을 받아오면 상태코드 200을 반환한다.")
    @Test
    void 일정_조율을_받아오면_상태코드_200을_반환한다() {
        // given
        String accessToken = 자체_토큰을_생성하고_토큰을_반환한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드);

        CategoryResponse 공통_일정_응답 = 새로운_카테고리를_등록한다(accessToken, 공통_일정_생성_요청).as(CategoryResponse.class);

        새로운_일정을_등록한다(accessToken, 공통_일정_응답.getId()).as(ScheduleResponse.class);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("startDateTime", "2022-07-01T00:00")
                .queryParam("endDateTime", "2022-07-31T00:00")
                .auth().oauth2(accessToken)
                .when().get("/api/scheduler/categories/{categoryId}/available-periods", 공통_일정_응답.getId())
                .then().log().all()
                .extract();

        // then
        상태코드_200이_반환된다(response);
    }
}
