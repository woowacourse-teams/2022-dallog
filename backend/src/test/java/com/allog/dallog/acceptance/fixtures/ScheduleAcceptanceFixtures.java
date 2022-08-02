package com.allog.dallog.acceptance.fixtures;

import com.allog.dallog.domain.schedule.dto.request.ScheduleCreateRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class ScheduleAcceptanceFixtures {

    public static ExtractableResponse<Response> 새로운_일정을_등록한다(final String accessToken,
                                                             final ScheduleCreateRequest request,
                                                             final Long categoryId) {

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(request)
                .when().post("/api/categories/{categoryId}/schedules", categoryId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 일정을_삭제한다(final String accessToken, final Long scheduleId) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/api/schedules/{scheduleId}", scheduleId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 일정_아이디로_일정을_단건_조회한다(final String accessToken, final Long scheduleId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().get("/api/schedules/{scheduleId}", scheduleId)
                .then().log().all()
                .extract();
    }
}
