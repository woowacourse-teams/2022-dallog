package com.allog.dallog.acceptance;

import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_200이_반환된다;
import static com.allog.dallog.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_201이_반환된다;
import static com.allog.dallog.acceptance.fixtures.ScheduleAcceptanceFixtures.새로운_일정을_등록한다;
import static com.allog.dallog.acceptance.fixtures.ScheduleAcceptanceFixtures.월별_일정을_조회한다;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.END_DATE_TIME_STRING;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.MEMO;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.MONTH;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.START_DATE_TIME_STRING;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.TITLE;
import static com.allog.dallog.common.fixtures.ScheduleFixtures.YEAR;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("일정 관련 기능")
class ScheduleAcceptanceTest extends AcceptanceTest {

    @DisplayName("정상적인 일정정보를 등록하면 상태코드 201을 반환한다.")
    @Test
    void 정상적인_일정정보를_등록하면_상태코드_201을_반환한다() {
        // given & when
        ExtractableResponse<Response> response = 새로운_일정을_등록한다(TITLE, START_DATE_TIME_STRING, END_DATE_TIME_STRING,
                MEMO);

        // then
        상태코드_201이_반환된다(response);
    }

    @DisplayName("월별 일정정보를 조회하면 상태코드 200을 반환한다.")
    @Test
    void 월별_일정정보를_조회하면_상태코드_200을_반환한다() {
        // given
        새로운_일정을_등록한다(TITLE, START_DATE_TIME_STRING, END_DATE_TIME_STRING, MEMO);
        새로운_일정을_등록한다(TITLE, START_DATE_TIME_STRING, END_DATE_TIME_STRING, MEMO);

        // when
        ExtractableResponse<Response> response = 월별_일정을_조회한다(YEAR, MONTH);

        // then
        상태코드_200이_반환된다(response);
    }
}
