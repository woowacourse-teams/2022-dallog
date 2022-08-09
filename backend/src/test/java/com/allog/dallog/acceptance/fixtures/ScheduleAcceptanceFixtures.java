package com.allog.dallog.acceptance.fixtures;

import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_메모;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.알록달록_회의_제목;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.테코톡_메모;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.테코톡_제목;

import com.allog.dallog.domain.schedule.dto.request.ScheduleUpdateRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class ScheduleAcceptanceFixtures {

    public static ExtractableResponse<Response> 새로운_일정을_등록한다(final String accessToken, final Long categoryId) {

        Map<String, String> params = new HashMap();
        params.put("title", 알록달록_회의_제목);
        params.put("startDateTime", "2022-07-04T13:00");
        params.put("endDateTime", "2022-07-05T16:00");
        params.put("memo", 알록달록_회의_메모);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(params)
                .when().post("/api/categories/{categoryId}/schedules", categoryId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 반복_일정을_등록한다(final String accessToken, final Long categoryId) {

        Map<String, String> scheduleParams = new HashMap();
        scheduleParams.put("title", 테코톡_제목);
        scheduleParams.put("startDateTime", "2022-08-04T14:00");
        scheduleParams.put("endDateTime", "2022-08-04T15:00");
        scheduleParams.put("memo", 테코톡_메모);

        Map<String, Object> scheduleRepeatParams = new HashMap<>();
        scheduleRepeatParams.put("schedule", scheduleParams);
        scheduleRepeatParams.put("endDate", "2022-08-31");
        scheduleRepeatParams.put("repeatType", "everyWeek");

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(scheduleRepeatParams)
                .when().post("/api/categories/{categoryId}/schedules/repeat", categoryId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 일정을_수정한다(final String accessToken, final Long scheduleId, final
    ScheduleUpdateRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(request)
                .when().patch("/api/schedules/{scheduleId}", scheduleId)
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

    public static ExtractableResponse<Response> 반복_일정에서_중간_일정_이후_모든_일정을_삭제한다(final String accessToken,
                                                                             final Long scheduleId) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/api/schedules/{scheduleId}?afterAll=true", scheduleId)
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
