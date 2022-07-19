package com.allog.dallog.acceptance.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class ScheduleAcceptanceFixture {

    public static ExtractableResponse<Response> 새로운_일정을_등록한다(final String title, final String startDateTime,
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

    public static ExtractableResponse<Response> 월별_일정을_조회한다(final int year, final int month) {
        return RestAssured.given().log().all()
                .when().get("/api/schedules?year={year}&month={month}", year, month)
                .then().log().all()
                .extract();
    }
}
