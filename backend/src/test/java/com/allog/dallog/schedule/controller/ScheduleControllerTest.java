package com.allog.dallog.schedule.controller;

import static com.allog.dallog.DocumentUtils.getRequestSpecification;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.allog.dallog.schedule.dto.request.ScheduleCreateRequest;
import io.restassured.RestAssured;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScheduleControllerTest extends ControllerTest {

    @DisplayName("일정 정보를 등록한다.")
    @Test
    void 일정_정보를_등록한다() {
        //given
        ScheduleCreateRequest request = new ScheduleCreateRequest("제목", LocalDateTime.now(), LocalDateTime.now(), "내용");

        // when & then
        RestAssured
                .given(getRequestSpecification()).log().all()
                .accept("application/json")
                .filter(document("schedule-save", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .contentType("application/json")
                .body(request)
                .when().post("/api/schedules")
                .then().log().all()
                .extract();
    }
}
