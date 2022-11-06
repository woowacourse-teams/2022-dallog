package com.allog.dallog.acceptance.builder;

import static com.allog.dallog.acceptance.fixtures.RestAssuredFixtures.getWithToken;
import static com.allog.dallog.acceptance.fixtures.RestAssuredFixtures.patchWithToken;
import static com.allog.dallog.acceptance.fixtures.StatusCodeValidateFixtures.validateNoContentStatus;
import static com.allog.dallog.acceptance.fixtures.StatusCodeValidateFixtures.validateOkStatus;
import static com.allog.dallog.common.fixtures.AuthFixtures.MEMBER_이름;
import static com.allog.dallog.common.fixtures.AuthFixtures.MEMBER_이메일;
import static com.allog.dallog.common.fixtures.AuthFixtures.MEMBER_프로필;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.domain.member.dto.response.MemberResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class MemberAssuredBuilder {

    private MemberAssuredBuilder() {
    }

    public static MemberRequestBuilder 요청() {
        return new MemberRequestBuilder();
    }

    public static class MemberRequestBuilder {

        private ExtractableResponse<Response> response;

        public MemberRequestBuilder 내_정보를_조회한다(final String accessToken) {
            response = getWithToken("/api/members/me", accessToken);
            return this;
        }

        public MemberRequestBuilder 내_이름을_변경한다(final String accessToken, final Object body) {
            response = patchWithToken(accessToken, body, "/api/members/me");
            return this;
        }

        public MemberResponseBuilder 응답() {
            return new MemberResponseBuilder(response);
        }
    }

    public static class MemberResponseBuilder {

        private final ExtractableResponse<Response> response;

        public MemberResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public MemberResponseBuilder 상태코드_200을_받는다() {
            validateOkStatus(response);
            return this;
        }

        public MemberResponseBuilder 상태코드_204을_받는다() {
            validateNoContentStatus(response);
            return this;
        }

        public MemberResponseBuilder 내_정보를_받는다() {
            MemberResponse actual = response.as(MemberResponse.class);
            assertAll(() -> {
                assertThat(actual.getEmail()).isEqualTo(MEMBER_이메일);
                assertThat(actual.getDisplayName()).isEqualTo(MEMBER_이름);
                assertThat(actual.getProfileImageUrl()).isEqualTo(MEMBER_프로필);
            });
            return this;
        }
    }
}
