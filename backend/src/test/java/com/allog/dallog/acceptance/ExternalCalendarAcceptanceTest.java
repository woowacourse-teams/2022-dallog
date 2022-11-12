package com.allog.dallog.acceptance;

import static com.allog.dallog.acceptance.fixtures.AuthAcceptanceFixtures.자체_토큰을_생성하고_엑세스_토큰을_반환한다;
import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_200이_반환된다;
import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_201이_반환된다;
import static com.allog.dallog.common.fixtures.AuthFixtures.GOOGLE_PROVIDER;
import static com.allog.dallog.common.fixtures.AuthFixtures.STUB_MEMBER_인증_코드;
import static com.allog.dallog.common.fixtures.ExternalCategoryFixtures.우아한테크코스_생성_요청;
import static com.allog.dallog.common.fixtures.ExternalCategoryFixtures.우아한테크코스_이름;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.category.dto.response.CategoryResponse;
import com.allog.dallog.externalcalendar.dto.ExternalCalendarsResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("외부 캘린더 관련 기능")
class ExternalCalendarAcceptanceTest extends AcceptanceTest {

    @DisplayName("자신의 외부 캘린더 리스트를 조회하면 200을 반환한다.")
    @Test
    void 자신의_외부_캘린더_리스트를_조회하면_200을_반환한다() {
        // given
        String accessToken = 자체_토큰을_생성하고_엑세스_토큰을_반환한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/external-calendars/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        ExternalCalendarsResponse externalCalendarsResponse = response.as(ExternalCalendarsResponse.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(response);
            assertThat(externalCalendarsResponse.getExternalCalendars()).hasSize(3);
        });
    }

    @DisplayName("외부 캘린더를 추가하면 201을 반환한다.")
    @Test
    void 외부_캘린더를_추가하면_201을_반환한다() {
        // given
        String accessToken = 자체_토큰을_생성하고_엑세스_토큰을_반환한다(GOOGLE_PROVIDER, STUB_MEMBER_인증_코드);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(우아한테크코스_생성_요청)
                .when().post("/api/external-calendars/me")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        CategoryResponse categoryResponse = response.as(CategoryResponse.class);

        // then
        assertAll(() -> {
            상태코드_201이_반환된다(response);
            assertThat(categoryResponse.getName()).isEqualTo(우아한테크코스_이름);
        });
    }
}
