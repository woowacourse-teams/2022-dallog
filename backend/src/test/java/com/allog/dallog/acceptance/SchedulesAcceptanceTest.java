package com.allog.dallog.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("일정 관련 기능")
public class SchedulesAcceptanceTest extends AcceptanceTest {

    @DisplayName("정상적인 일정정보를 등록하면 상태코드 201을 반환한다.")
    @Test
    public void 정상적인_일정정보를_등록하면_상태코드_201을_반환한다() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("title", "알록달록 회의");
        params.put("startDateTime", "2022-07-04T13:00");
        params.put("endDateTime", "2022-07-05T07:00");
        params.put("memo", "알록달록 회의가 있어요");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/api/schedules")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
