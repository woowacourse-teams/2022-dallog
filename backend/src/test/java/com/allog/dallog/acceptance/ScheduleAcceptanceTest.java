package com.allog.dallog.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("일정 관련 기능")
class ScheduleAcceptanceTest extends AcceptanceTest {

    @DisplayName("정상적인 일정정보를 등록하면 상태코드 201을 반환한다.")
    @Test
    void 정상적인_일정정보를_등록하면_상태코드_201을_반환한다() {
        // given
        String title = "알록달록 회의";
        String startDateTime = "2022-07-04T13:00";
        String endDateTime = "2022-07-05T07:00";
        String memo = "알록달록 회의가 있어요";

        // when
        ExtractableResponse<Response> response = 새로운_일정을_등록한다(title, startDateTime, endDateTime, memo);

        // then
        상태코드_201이_반환된다(response);
    }

    @DisplayName("월별 일정정보를 조회하면 상태코드 200을 반환한다.")
    @Test
    void 월별_일정정보를_조회하면_상태코드_200을_반환한다() {
        // given
        int year = 2022;
        int month = 7;

        새로운_일정을_등록한다("알록달록 회의 1", "2022-07-04T13:00", "2022-07-05T07:00", "알록달록 회의 1이 있어요");
        새로운_일정을_등록한다("알록달록 회의 2", "2022-08-04T13:00", "2022-08-05T07:00", "알록달록 회의 2이 있어요");

        // when
        ExtractableResponse<Response> response = 월별_일정을_조회한다(year, month);

        // then
        상태코드_200이_반환된다(response);
    }

    private ExtractableResponse<Response> 새로운_일정을_등록한다(final String title, final String startDateTime,
                                                       final String endDateTime, final String memo) {
        Map<String, String> params = new HashMap<>();
        params.put("title", title);
        params.put("startDateTime", startDateTime);
        params.put("endDateTime", endDateTime);
        params.put("memo", memo);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/schedules")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 월별_일정을_조회한다(final int year, final int month) {
        return RestAssured.given().log().all()
                .when().get("/api/schedules?year={year}&month={month}", year, month)
                .then().log().all()
                .extract();
    }
}
