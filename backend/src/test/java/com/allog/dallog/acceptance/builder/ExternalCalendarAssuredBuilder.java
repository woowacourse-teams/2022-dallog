package com.allog.dallog.acceptance.builder;

import static com.allog.dallog.acceptance.fixtures.RestAssuredFixtures.getWithToken;
import static com.allog.dallog.acceptance.fixtures.RestAssuredFixtures.postWithToken;
import static com.allog.dallog.acceptance.fixtures.StatusCodeValidateFixtures.validateCreatedStatus;
import static com.allog.dallog.acceptance.fixtures.StatusCodeValidateFixtures.validateOkStatus;
import static org.assertj.core.api.Assertions.assertThat;

import com.allog.dallog.domain.externalcalendar.dto.ExternalCalendarsResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class ExternalCalendarAssuredBuilder {

    private ExternalCalendarAssuredBuilder() {
    }

    public static ExternalCalendarRequestBuilder 요청() {
        return new ExternalCalendarRequestBuilder();
    }

    public static class ExternalCalendarRequestBuilder {

        private ExtractableResponse<Response> response;

        public ExternalCalendarRequestBuilder 자신의_외부_캘린더_목록을_조회한다(final String accessToken) {
            response = getWithToken("/api/external-calendars/me", accessToken);
            return this;
        }

        public ExternalCalendarRequestBuilder 외부_캘린더를_추가한다(final String accessToken, final Object body) {
            response = postWithToken(accessToken, "/api/external-calendars/me", body);
            return this;
        }

        public ExternalCalendarResponseBuilder 응답() {
            return new ExternalCalendarResponseBuilder(response);
        }
    }

    public static class ExternalCalendarResponseBuilder {

        private final ExtractableResponse<Response> response;

        public ExternalCalendarResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public ExternalCalendarResponseBuilder 상태코드_200을_받는다() {
            validateOkStatus(response);
            return this;
        }

        public ExternalCalendarResponseBuilder 상태코드_201을_받는다() {
            validateCreatedStatus(response);
            return this;
        }

        public void 자신의_외부_캘린더_목록을_받는다(int calendarCount) {
            ExternalCalendarsResponse actual = response.as(ExternalCalendarsResponse.class);
            assertThat(actual.getExternalCalendars()).hasSize(calendarCount);
        }
    }
}
