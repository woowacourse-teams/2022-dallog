package com.allog.dallog.acceptance.builder;

import static com.allog.dallog.acceptance.fixtures.RestAssuredFixtures.get;
import static com.allog.dallog.acceptance.fixtures.RestAssuredFixtures.getWithToken;
import static com.allog.dallog.acceptance.fixtures.RestAssuredFixtures.post;
import static com.allog.dallog.acceptance.fixtures.StatusCodeValidateFixtures.validateOkStatus;
import static com.allog.dallog.acceptance.fixtures.StatusCodeValidateFixtures.validateUnauthorizedStatus;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.allog.dallog.domain.auth.dto.request.TokenRenewalRequest;
import com.allog.dallog.domain.auth.dto.request.TokenRequest;
import com.allog.dallog.domain.auth.dto.response.AccessAndRefreshTokenResponse;
import com.allog.dallog.domain.auth.dto.response.AccessTokenResponse;
import com.allog.dallog.domain.auth.dto.response.OAuthUriResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class AuthAssuredBuilder {

    private AuthAssuredBuilder() {
    }

    public static AuthRequestBuilder 요청() {
        return new AuthRequestBuilder();
    }

    public static class AuthRequestBuilder {

        private ExtractableResponse<Response> response;

        public AuthRequestBuilder OAuth_인증_URI을_요청한다(final String oauthProvider) {
            response = get("/api/auth/{oauthProvider}/oauth-uri?redirectUri={redirectUri}", oauthProvider,
                    "https://dallog.me/oauth");
            return this;
        }

        public AuthRequestBuilder 회원가입_한다(String oauthProvider, String code) {
            Object body = new TokenRequest(code, "https://dallog.me/oauth");
            response = post("/api/auth/{oauthProvider}/token", body, oauthProvider);
            return this;
        }

        public AuthRequestBuilder 로그인_한다(String oauthProvider, String code) {
            회원가입_한다(oauthProvider, code);
            return this;
        }

        public AuthRequestBuilder 만료된_토큰으로_API를_요청한다() {
            String accessToken = response.as(AccessAndRefreshTokenResponse.class).getAccessToken();
            response = getWithToken("/api/auth/validate/token", accessToken);
            return this;
        }

        public AuthRequestBuilder 엑세스_토큰_재발급을_요청한다() {
            Object body = new TokenRenewalRequest(response.as(AccessAndRefreshTokenResponse.class).getRefreshToken());
            response = post("/api/auth/token/access", body);
            return this;
        }

        public AuthResponseBuilder 응답() {
            return new AuthResponseBuilder(response);
        }
    }

    public static class AuthResponseBuilder {

        private final ExtractableResponse<Response> response;

        public AuthResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public AuthResponseBuilder 상태코드_200을_받는다() {
            validateOkStatus(response);
            return this;
        }

        public AuthResponseBuilder 상태코드_401을_받는다() {
            validateUnauthorizedStatus(response);
            return this;
        }

        public AuthResponseBuilder OAuth_인증_URL에_접속한다() {
            OAuthUriResponse actual = response.as(OAuthUriResponse.class);
            assertThat(actual.getoAuthUri()).contains("https://");
            return this;
        }

        public AuthResponseBuilder 토큰들을_발급_받는다() {
            AccessAndRefreshTokenResponse actual = response.as(AccessAndRefreshTokenResponse.class);
            assertAll(() -> {
                assertThat(actual.getAccessToken()).isNotEmpty();
                assertThat(actual.getRefreshToken()).isNotEmpty();
            });
            return this;
        }

        public AuthResponseBuilder 엑세스_토큰을_발급_받는다() {
            AccessTokenResponse actual = response.as(AccessTokenResponse.class);
            assertThat(actual.getAccessToken()).isNotEmpty();
            return this;
        }

        public String accessToken() {
            return response.as(AccessAndRefreshTokenResponse.class).getAccessToken();
        }
    }
}
