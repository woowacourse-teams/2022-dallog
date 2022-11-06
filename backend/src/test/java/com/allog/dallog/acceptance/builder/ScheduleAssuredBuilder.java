package com.allog.dallog.acceptance.builder;

import static com.allog.dallog.acceptance.fixtures.RestAssuredFixtures.deleteWithToken;
import static com.allog.dallog.acceptance.fixtures.RestAssuredFixtures.getWithToken;
import static com.allog.dallog.acceptance.fixtures.RestAssuredFixtures.patchWithToken;
import static com.allog.dallog.acceptance.fixtures.RestAssuredFixtures.postWithToken;
import static com.allog.dallog.acceptance.fixtures.StatusCodeValidateFixtures.validateCreatedStatus;
import static com.allog.dallog.acceptance.fixtures.StatusCodeValidateFixtures.validateNoContentStatus;
import static com.allog.dallog.acceptance.fixtures.StatusCodeValidateFixtures.validateOkStatus;

import com.allog.dallog.domain.schedule.dto.response.ScheduleResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class ScheduleAssuredBuilder {

    private ScheduleAssuredBuilder() {
    }

    public static ScheduleRequestBuilder 요청() {
        return new ScheduleRequestBuilder();
    }

    public static class ScheduleRequestBuilder {

        private ExtractableResponse<Response> response;

        public ScheduleRequestBuilder 일정을_등록한다(final String accessToken, final Object body, final Long categoryId) {
            response = postWithToken(accessToken, "/api/categories/{categoryId}/schedules", body,
                    String.valueOf(categoryId));
            return this;
        }

        public ScheduleRequestBuilder 단건_일정을_조회한다(final String accessToken) {
            Long scheduleId = response.as(ScheduleResponse.class).getId();
            response = getWithToken("/api/schedules/{scheduleId}", accessToken, String.valueOf(scheduleId));
            return this;
        }

        public ScheduleRequestBuilder 일정을_수정한다(final String accessToken, final Object body) {
            Long scheduleId = response.as(ScheduleResponse.class).getId();
            response = patchWithToken(accessToken, body, "/api/schedules/{scheduleId}", String.valueOf(scheduleId));
            return this;
        }

        public ScheduleRequestBuilder 일정을_삭제한다(final String accessToken) {
            Long scheduleId = response.as(ScheduleResponse.class).getId();
            response = deleteWithToken(accessToken, "/api/schedules/{scheduleId}", String.valueOf(scheduleId));
            return this;
        }

        public ScheduleRequestBuilder 월별_일정을_조회한다(final String accessToken, final Long categoryId, final String startDate,
                                final String endDate) {
            response = getWithToken(
                    "/api/categories/{categoryId}/schedules?startDateTime={startDateTime}&endDateTime={endDateTime}",
                    accessToken, String.valueOf(categoryId), startDate, endDate);
            return this;
        }

        public ScheduleResponseBuilder 응답() {
            return new ScheduleResponseBuilder(response);
        }
    }

    public static class ScheduleResponseBuilder {

        private final ExtractableResponse<Response> response;

        public ScheduleResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public ScheduleResponseBuilder 상태코드_200을_받는다() {
            validateOkStatus(response);
            return this;
        }

        public ScheduleResponseBuilder 상태코드_201을_받는다() {
            validateCreatedStatus(response);
            return this;
        }

        public ScheduleResponseBuilder 상태코드_204을_받는다() {
            validateNoContentStatus(response);
            return this;
        }
    }
}