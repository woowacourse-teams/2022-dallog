package com.allog.dallog.schedule.controller;

import static com.allog.dallog.DocumentUtils.getRequestSpecification;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class ScheduleControllerTest extends ControllerTest {

    @DisplayName("일정 정보를 등록한다.")
    @Test
    void 일정_정보를_등록한다() {
        //given
        String title = "팀 회식";
        String startDateTime = "2022-07-04T18:00";
        String endDateTime = "2022-07-04T21:00";
        String memo = "18시 호프집 회식 - 인원 6명";

        // when
        ExtractableResponse<Response> response = 새로운_일정을_등록한다(title, startDateTime, endDateTime, memo);

        // then
        상태코드_201이_반환된다(response);
    }


    @DisplayName("월별 일정 정보를 조회한다.")
    @Test
    void 월별_일정_정보를_조회한다() {
        // given
        int year = 2022;
        int month = 7;

        새로운_일정을_등록한다("7월 팀 회식", "2022-07-04T18:00", "2022-07-04T21:00", "18시 호프집 회식 - 인원 6명");
        새로운_일정을_등록한다("8월 팀 회식", "2022-08-07T18:00", "2022-08-07T21:00", "18시 치킨집 회식 - 인원 9명");

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

        return RestAssured
                .given(getRequestSpecification()).log().all()
                .accept("application/json")
                .filter(document("schedule/save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()
                        )))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/schedules")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 월별_일정을_조회한다(final int year, final int month) {
        return RestAssured
                .given(getRequestSpecification()).log().all()
                .accept("application/json")
                .filter(document("schedule/find",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("year").description("일정을 조회할 년도"),
                                parameterWithName("month").description("일정을 조회할 월")
                        )))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/schedules?year={year}&month={month}", year, month)
                .then().log().all()
                .extract();
    }

    private void 상태코드_200이_반환된다(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 상태코드_201이_반환된다(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
